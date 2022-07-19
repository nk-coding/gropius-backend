package gropius.repository.architecture

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.architecture.AffectedByIssue

/**
 * Repository for [AffectedByIssue]
 */
@Repository
interface AffectedByIssueRepository : ReactiveNeo4jRepository<AffectedByIssue, String>