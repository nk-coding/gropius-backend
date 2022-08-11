package gropius.repository.template

import gropius.model.template.IssueRelationType
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [IssueRelationType]
 */
@Repository
interface IssueRelationTypeRepository : ReactiveNeo4jRepository<IssueRelationType, String>