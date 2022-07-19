package gropius.repository.template

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.template.IssueRelationType

/**
 * Repository for [IssueRelationType]
 */
@Repository
interface IssueRelationTypeRepository : ReactiveNeo4jRepository<IssueRelationType, String>