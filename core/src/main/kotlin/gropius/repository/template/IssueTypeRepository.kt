package gropius.repository.template

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.template.IssueType

/**
 * Repository for [IssueType]
 */
@Repository
interface IssueTypeRepository : ReactiveNeo4jRepository<IssueType, String>