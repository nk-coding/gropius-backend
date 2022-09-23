package gropius.sync.github

import gropius.model.issue.Issue
import gropius.model.issue.Label
import gropius.model.issue.timeline.Body
import gropius.model.template.IssueTemplate
import gropius.model.template.IssueType
import gropius.model.user.IMSUser
import gropius.model.user.User
import gropius.sync.JsonHelper
import gropius.sync.github.config.IMSProjectConfig
import gropius.sync.github.generated.fragment.*
import gropius.sync.github.generated.fragment.UserData.Companion.asNode
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
 * Save GitHub nodes as Gropius nodes into the database
 * @param helper Reference for the spring instance of JsonHelper
 * @param neoOperations Reference for the spring instance of ReactiveNeo4jOperations
 * @param issueInfoRepository Reference for the spring instance of IssueInfoRepository
 * @param userInfoRepository Reference for the spring instance of UserInfoRepository
 * @param labelInfoRepository Reference for the spring instance of LabelInfoRepository
 * @param tokenManager Reference for the spring instance of TokenManager
 */
@Component
class NodeSourcerer(
    @Qualifier("graphglueNeo4jOperations")
    private val neoOperations: ReactiveNeo4jOperations,
    private val issueInfoRepository: IssueInfoRepository,
    private val userInfoRepository: UserInfoRepository,
    private val helper: JsonHelper,
    private val labelInfoRepository: LabelInfoRepository,
    private val tokenManager: TokenManager
) {
    /**
     * Ensure the default GitHub issue type with the default template is in the database
     * @return The default type for GitHub issues
     */
    suspend fun ensureGithubType(): IssueType {
        val types = neoOperations.findAll<IssueType>().toList()
        for (type in types) {
            if (type.name == "github-issue") {
                return type
            }
        }
        var type = IssueType("github-issue", "Issue synced from GitHub")
        type.partOf() += ensureGithubTemplate()
        type = neoOperations.save(type).awaitSingle()
        return type
    }

    /**
     * Ensure the default GitHub template is in the databse
     * @return The default template for GitHub issues
     */
    suspend fun ensureGithubTemplate(): IssueTemplate {
        val types = neoOperations.findAll<IssueTemplate>().toList()
        for (type in types) {
            if (type.name == "github-temp") {
                return type
            }
        }
        var template = IssueTemplate("github-temp", "Github Template", mutableMapOf(), false)
        template = neoOperations.save(template).awaitSingle()
        return template
    }

    /**
     * Prepare an IssueBody for the given issue is in the database
     * @param info The issue to be created this body for
     * @param imsProjectConfig Config of the active project
     * @return The finished body
     */
    private suspend fun prepareIssueBody(imsProjectConfig: IMSProjectConfig, info: IssueData): Body {
        val user = ensureUser(imsProjectConfig, info.author!!)
        var issueBody = Body(
            info.createdAt, info.createdAt, (info as? IssueDataExtensive)?.body ?: "", info.createdAt
        )
        issueBody.createdBy().value = user
        issueBody.lastModifiedBy().value = user
        issueBody.bodyLastEditedBy().value = user
        return issueBody
    }

    /**
     * Fills the body of the issue with the data from info and save the resulting issue
     * @param imsProjectConfig the config to use while processing the issue
     * @param info the data from the GitHub api
     * @param issue the issue to start working with
     * @return true if the content of the issue has been changed
     */
    private suspend fun fillIssueBodyAndSave(
        imsProjectConfig: IMSProjectConfig, info: IssueData, issue: Issue
    ): Boolean {
        var bodyChanged = false
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
                bodyChanged = true
            }
            issue.body().value = issueBody
        }
        issue.trackables().add(imsProjectConfig.imsProject.trackable().value)
        neoOperations.save(issue).awaitSingle()
        return bodyChanged
    }

    /**
     * Fills the direct part an Issue structure with the data from info
     * @param imsProjectConfig the config to use while processing the issue
     * @param info the data from the GitHub api
     * @return the resulting Issue
     */
    private suspend fun prepareIssueFromIssueData(imsProjectConfig: IMSProjectConfig, info: IssueData): Issue {
        val issue = Issue(
            info.createdAt,
            OffsetDateTime.now(),
            mutableMapOf(),
            info.title,
            info.createdAt,
            true,
            null,
            null,
            null,
            null
        )
        issue.body().value = prepareIssueBody(imsProjectConfig, info)
        issue.body().value.issue().value = issue
        issue.createdBy().value = ensureUser(imsProjectConfig, info.author!!)
        issue.lastModifiedBy().value = ensureUser(imsProjectConfig, info.author!!)
        issue.type().value = ensureGithubType()
        issue.template().value = ensureGithubTemplate()
        return issue
    }

    /**
     * Ensure a given issue is in the database
     * @param info The issue to be created this body for
     * @param imsProjectConfig Config of the active project
     * @return The Gropius issue and the mongodb issue mapping
     */
    suspend fun ensureIssue(imsProjectConfig: IMSProjectConfig, info: IssueData): Pair<Issue, IssueInfo> {
        var issueInfo = issueInfoRepository.findByUrlAndGithubId(imsProjectConfig.url, info.id)
        val issue: Issue = if (issueInfo == null) {
            prepareIssueFromIssueData(imsProjectConfig, info)
        } else {
            neoOperations.findById(issueInfo.neo4jId)!!
        }
        val bodyChanged = fillIssueBodyAndSave(imsProjectConfig, info, issue)
        if ((issueInfo == null) || bodyChanged) {
            issueInfo = issueInfoRepository.save(
                issueInfo ?: IssueInfo(
                    info.id, imsProjectConfig.url, issue.rawId!!, true, null, info, null
                )
            ).awaitSingle()
        }
        return Pair(issue, issueInfo!!)
    }

    /**
     * Ensure the user with the given UserData is inserted in the database
     * @param info The GraphQL user data
     * @param imsProjectConfig Config of the active project
     * @return a Gropius user
     */
    suspend fun ensureUser(imsProjectConfig: IMSProjectConfig, info: UserData) =
        ensureUser(imsProjectConfig, info.login, info.asNode()?.id)

    /**
     * Ensure a user with the given username is in the database
     * @param username The GitHub username string
     * @param imsProjectConfig Config of the active project
     * @return a Gropius user
     */
    suspend fun ensureUser(imsProjectConfig: IMSProjectConfig, username: String, githubId: String? = null): User {
        val userInfo = userInfoRepository.findByUrlAndLogin(imsProjectConfig.url, username)
        return if (userInfo == null) {
            var user = IMSUser(
                username,
                null,
                username,
                mutableMapOf("github_id" to (helper.objectMapper.writeValueAsString(githubId) ?: "null"))
            )
            user.ims().value = imsProjectConfig.imsConfig.ims
            user = neoOperations.save(user).awaitSingle()
            userInfoRepository.save(UserInfo(username, user.rawId!!, imsProjectConfig.url)).awaitSingle()
            tokenManager.advertiseIMSUser(user)
            user
        } else {
            neoOperations.findById(userInfo.neo4jId)!!
        }
    }

    /**
     * Ensure a given label is in the database
     * @param info The GraphQL data for this label
     * @param imsProjectConfig Config of the active project
     * @return a Gropius label
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
            neoOperations.findById(labelInfo.neo4jId)!!
        }
    }

}
