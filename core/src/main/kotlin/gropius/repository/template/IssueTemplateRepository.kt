package gropius.repository.template

import gropius.model.template.IssueTemplate
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [IssueTemplate]
 */
@Repository
interface IssueTemplateRepository : ReactiveNeo4jRepository<IssueTemplate, String>