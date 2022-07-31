package gropius.repository.template

import gropius.model.template.IMSTemplate
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [IMSTemplate]
 */
@Repository
interface IMSTemplateRepository : ReactiveNeo4jRepository<IMSTemplate, String>