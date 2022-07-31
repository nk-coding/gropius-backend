package gropius.repository.issue

import gropius.model.issue.Issue
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [Issue]
 */
@Repository
interface IssueRepository : ReactiveNeo4jRepository<Issue, String>