package gropius.repository.architecture

import gropius.model.architecture.IMSIssue
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [IMSIssue]
 */
@Repository
interface IMSIssueRepository : ReactiveNeo4jRepository<IMSIssue, String>