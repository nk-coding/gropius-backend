package gropius.service.issue

import gropius.model.issue.Issue
import gropius.repository.issue.IssueRepository
import gropius.service.common.AuditedNodeService
import org.springframework.stereotype.Service

/**
 * Service for [Issue]s. Provides function to update the deprecation status
 *
 * @param repository the associated repository used for CRUD functionality
 */
@Service
class IssueService(repository: IssueRepository) : AuditedNodeService<Issue, IssueRepository>(repository) {

    /**
     * Deletes an [Issue]
     * Does not check the authorization status
     *
     * @param node the Issue to delete
     * @param permanently if `false`, the issue is only marked as deleted, otherwise completely deleted
     */
    suspend fun deleteIssue(node: Issue, permanently: Boolean) {
        // TODO
    }

}