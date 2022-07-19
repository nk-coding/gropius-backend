package gropius.repository.template

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.template.IMSTemplate

/**
 * Repository for [IMSTemplate]
 */
@Repository
interface IMSTemplateRepository : ReactiveNeo4jRepository<IMSTemplate, String>