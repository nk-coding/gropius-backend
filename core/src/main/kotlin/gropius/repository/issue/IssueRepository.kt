package gropius.repository.issue

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.issue.Issue

/**
 * Repository for [Issue]
 */
@Repository
interface IssueRepository : ReactiveNeo4jRepository<Issue, String>