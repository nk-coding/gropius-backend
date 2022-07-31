package gropius.repository.architecture

import gropius.model.architecture.AffectedByIssue
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [AffectedByIssue]
 */
@Repository
interface AffectedByIssueRepository : ReactiveNeo4jRepository<AffectedByIssue, String>