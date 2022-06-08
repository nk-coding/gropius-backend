package gropius.sync.github

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

@Component
class NodeSourcerer(
    @Qualifier("graphglueNeo4jOperations")
    private val neoOperations: ReactiveNeo4jOperations,
    private val issueInfoRepository: IssueInfoRepository,
    private val userInfoRepository: UserInfoRepository,
    private val labelInfoRepository: LabelInfoRepository
) {
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

    private suspend fun createIssueBody(info: IssueData): Body {
        val user = ensureUser(info.author!!)
        val issueBody = Body(
            info.createdAt, OffsetDateTime.now(), (info as? IssueDataExtensive)?.body ?: "", info.createdAt
        )
        issueBody.createdBy().value = user
        issueBody.lastModifiedBy().value = user
        issueBody.bodyLastEditedBy().value = user
        return issueBody
    }

    suspend fun ensureIssue(info: IssueData): Pair<Issue, IssueInfo> {
        val issueInfo = issueInfoRepository.findByGithubId(info.id)
        if (issueInfo == null) {
            var issue = Issue(
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
            val user = ensureUser(info.author!!)
            val issueBody = createIssueBody(info)
            issue.body().value = issueBody
            issueBody.issue().value = issue
            issue.createdBy().value = user
            issue.lastModifiedBy().value = user
            issue.type().value = ensureGithubType()
            issue.template().value = ensureGithubTemplate()
            issue = neoOperations.save(issue).awaitSingle()
            return Pair(issue, issueInfoRepository.save(IssueInfo(info.id, issue.rawId!!, true, null)).awaitSingle())
        } else {
            return Pair(neoOperations.findById<Issue>(issueInfo.neo4jId)!!, issueInfo)
        }
    }

    suspend fun ensureUser(info: UserData) = ensureUser(info.login)

    suspend fun ensureBotUser() = ensureUser("github-bot")//TODO: Read from template

    suspend fun ensureUser(username: String): User {
        val userInfo = userInfoRepository.findByLogin(username)
        return if (userInfo == null) {
            var user = GropiusUser(username, null, username, false)
            user = neoOperations.save(user).awaitSingle()
            userInfoRepository.save(UserInfo(username, user.rawId!!)).awaitSingle()
            user
        } else {
            neoOperations.findById<User>(userInfo.neo4jId)!!
        }
    }

    suspend fun ensureLabel(info: LabelData): Label {
        val labelInfo = labelInfoRepository.findByGithubId(info.id)
        return if (labelInfo == null) {
            var label = Label(
                info.createdAt!!,
                OffsetDateTime.now(),
                info.name,
                (info as? LabelDataExtensive)?.description ?: "",
                (info as? LabelDataExtensive)?.color ?: "#000000"
            )
            label.createdBy().value = ensureBotUser()
            label.lastModifiedBy().value = ensureBotUser()
            label = neoOperations.save(label).awaitSingle()
            labelInfoRepository.save(LabelInfo(info.id, label.rawId!!)).awaitSingle()
            label
        } else {
            neoOperations.findById<Label>(labelInfo.neo4jId)!!
        }
    }

}