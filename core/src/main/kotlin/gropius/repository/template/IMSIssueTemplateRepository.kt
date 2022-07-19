package gropius.repository.template

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.template.IMSIssueTemplate

/**
 * Repository for [IMSIssueTemplate]
 */
@Repository
interface IMSIssueTemplateRepository : ReactiveNeo4jRepository<IMSIssueTemplate, String>