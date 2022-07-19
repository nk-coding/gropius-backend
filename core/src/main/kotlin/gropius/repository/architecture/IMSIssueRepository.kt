package gropius.repository.architecture

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.architecture.IMSIssue

/**
 * Repository for [IMSIssue]
 */
@Repository
interface IMSIssueRepository : ReactiveNeo4jRepository<IMSIssue, String>