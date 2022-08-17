package gropius.repository.issue.timeline

import gropius.model.issue.timeline.IssueRelation
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [IssueRelation]
 */
@Repository
interface IssueRelationRepository : ReactiveNeo4jRepository<IssueRelation, String>