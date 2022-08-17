package gropius.repository.template

import gropius.model.template.IssueType
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [IssueType]
 */
@Repository
interface IssueTypeRepository : ReactiveNeo4jRepository<IssueType, String>