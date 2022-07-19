package gropius.repository.template

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.template.IMSProjectTemplate

/**
 * Repository for [IMSProjectTemplate]
 */
@Repository
interface IMSProjectTemplateRepository : ReactiveNeo4jRepository<IMSProjectTemplate, String>