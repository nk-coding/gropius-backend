package gropius.repository.issue

import gropius.model.issue.Issue
import gropius.repository.GropiusRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [Issue]
 */
@Repository
interface IssueRepository : GropiusRepository<Issue, String>