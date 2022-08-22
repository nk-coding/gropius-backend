package gropius.sync.github

import gropius.model.architecture.IMSProject
import gropius.model.issue.Issue
import gropius.model.issue.Label
import gropius.model.issue.timeline.Body
import gropius.model.template.IssueTemplate
import gropius.model.template.IssueType
import gropius.model.user.GropiusUser
import gropius.model.user.User
import gropius.sync.github.generated.fragment.*
import gropius.sync.github.model.IssueInfo
import gropius.sync.github.model.LabelInfo
import gropius.sync.github.model.UserInfo
import gropius.sync.github.repository.IssueInfoRepository
import gropius.sync.github.repository.LabelInfoRepository
import gropius.sync.github.repository.UserInfoRepository
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.neo4j.core.ReactiveNeo4jOperations
import org.springframework.data.neo4j.core.findAll
import org.springframework.data.neo4j.core.findById
import org.springframework.stereotype.Component
import java.time.OffsetDateTime

/**
 * Save github nodes as gropius nodes into the database
 */
@Component
class NodeSourcerer(
    /**
     * Reference for the spring instance of ReactiveNeo4jOperations
     */
    @Qualifier("graphglueNeo4jOperations")
    private val neoOperations: ReactiveNeo4jOperations,
    /**
     * Reference for the spring instance of IssueInfoRepository
     */
    private val issueInfoRepository: IssueInfoRepository,
    /**
     * Reference for the spring instance of UserInfoRepository
     */
    private val userInfoRepository: UserInfoRepository,
    /**
     * Reference for the spring instance of LabelInfoRepository
     */
    private val labelInfoRepository: LabelInfoRepository
) {
    /**
     * Ensure the default github issue type with the default template is in the database
     * @return The default type for github issues
     */
    suspend fun ensureGithubType(): IssueType {
        val types = neoOperations.findAll<IssueType>().toList()
        for (type in types) {
            if (type.name == "github-issue") {
                return type
            }
        }
        var type = IssueType("github-issue", "Issue synced from github")
        type.partOf() += ensureGithubTemplate()
        type = neoOperations.save(type).awaitSingle()
        return type
    }

    /**
     * Ensure the default github template is in the databse
     * @return The default template for github issues
     */
    suspend fun ensureGithubTemplate(): IssueTemplate {
        val types = neoOperations.findAll<IssueTemplate>().toList()
        for (type in types) {
            if (type.name == "github-temp") {
                return type
            }
        }
        var template = IssueTemplate("github-temp", "Github Template", mutableMapOf<String, String>(), false)
        template = neoOperations.save(template).awaitSingle()
        return template
    }

    /**
     * Create an issuebody for the given issue is in the database
     * @param info The issue to created this body for
     * @param imsProjectConfig Config of the active project
     * @return The finished body
     */
    private suspend fun createIssueBody(imsProjectConfig: IMSProjectConfig, info: IssueData): Body {
        val user = ensureUser(imsProjectConfig, info.author!!)
        val issueBody = Body(
            info.createdAt, info.createdAt, (info as? IssueDataExtensive)?.body ?: "", info.createdAt
        )
        issueBody.createdBy().value = user
        issueBody.lastModifiedBy().value = user
        issueBody.bodyLastEditedBy().value = user
        return issueBody
    }

    /**
     * Ensure a given issue is in the database
     * @param info The issue to created this body for
     * @param imsProjectConfig Config of the active project
     * @return The gropius issue and the mongodb issue mapping
     */
    suspend fun ensureIssue(imsProjectConfig: IMSProjectConfig, info: IssueData): Pair<Issue, IssueInfo> {
        var issueInfo = issueInfoRepository.findByUrlAndGithubId(imsProjectConfig.url, info.id)
        var issue: Issue
        var needSave = false
        if (issueInfo == null) {
            issue = Issue(
                info.createdAt,
                OffsetDateTime.now(),
                mutableMapOf<String, String>(),
                info.title,
                info.createdAt,
                true,
                null,
                null,
                null,
                null
            )
            val user = ensureUser(imsProjectConfig, info.author!!)
            val issueBody = createIssueBody(imsProjectConfig, info)
            issue.body().value = issueBody
            issueBody.issue().value = issue
            issue.createdBy().value = user
            issue.lastModifiedBy().value = user
            issue.type().value = ensureGithubType()
            issue.template().value = ensureGithubTemplate()
        } else {
            issue = neoOperations.findById<Issue>(issueInfo.neo4jId)!!
        }
        if (info is IssueDataExtensive) {
            val user = ensureUser(imsProjectConfig, (info.editor ?: info.author)!!)
            val issueBody = issue.body().value
            if (issueBody.lastModifiedAt < (info.lastEditedAt ?: info.createdAt)) {
                issueBody.lastModifiedAt = (info.lastEditedAt ?: info.createdAt)
                issue.lastUpdatedAt = maxOf(issue.lastUpdatedAt, (info.lastEditedAt ?: info.createdAt))
                issueBody.bodyLastEditedAt = (info.lastEditedAt ?: info.createdAt)
                issueBody.lastModifiedBy().value = user
                issueBody.bodyLastEditedBy().value = user
                issueBody.body = info.body
                needSave = true
            }
            issue.body().value = issueBody
        }
        issue.trackables().add(imsProjectConfig.imsProject.trackable().value)
        issue = neoOperations.save(issue).awaitSingle()
        if ((issueInfo == null) || needSave) {
            issueInfo = issueInfoRepository.save(
                issueInfo ?: IssueInfo(
                    info.id, imsProjectConfig.url, issue.rawId!!, true, null,
                )
            ).awaitSingle()
        }
        return Pair(issue, issueInfo!!)
    }

    /**
     * Ensure the user with the given UserData is inserted in the database
     * @param info The GraphQL user data
     * @param imsProjectConfig Config of the active project
     * @return a gropius user
     */
    suspend fun ensureUser(imsProjectConfig: IMSProjectConfig, info: UserData) =
        ensureUser(imsProjectConfig, info.login)

    /**
     * Ensure a user with the given username is in the database
     * @param username The github username string
     * @param imsProjectConfig Config of the active project
     * @return a gropius user
     */
    suspend fun ensureUser(imsProjectConfig: IMSProjectConfig, username: String): User {
        val userInfo = userInfoRepository.findByUrlAndLogin(imsProjectConfig.url, username)
        return if (userInfo == null) {
            var user = GropiusUser(username, null, username, false)
            user = neoOperations.save(user).awaitSingle()
            userInfoRepository.save(UserInfo(username, user.rawId!!, imsProjectConfig.url)).awaitSingle()
            user
        } else {
            neoOperations.findById<User>(userInfo.neo4jId)!!
        }
    }

    /**
     * Ensure a given label is in the database
     * @param info The GraphQL data for this label
     * @param imsProjectConfig Config of the active project
     * @return a gropius label
     */
    suspend fun ensureLabel(
        imsProjectConfig: IMSProjectConfig, info: LabelData
    ): Label {
        val labelInfo = labelInfoRepository.findByUrlAndGithubId(imsProjectConfig.url, info.id)
        return if (labelInfo == null) {
            var label = Label(
                info.createdAt!!,
                OffsetDateTime.now(),
                info.name,
                (info as? LabelDataExtensive)?.description ?: "",
                (info as? LabelDataExtensive)?.color ?: "#000000"
            )
            label.createdBy().value = ensureUser(imsProjectConfig, imsProjectConfig.botUser)
            label.lastModifiedBy().value = ensureUser(imsProjectConfig, imsProjectConfig.botUser)
            label = neoOperations.save(label).awaitSingle()
            labelInfoRepository.save(LabelInfo(info.id, label.rawId!!, imsProjectConfig.url)).awaitSingle()
            label
        } else {
            neoOperations.findById<Label>(labelInfo.neo4jId)!!
        }
    }

}