package gropius.repository.template

import gropius.model.template.SubTemplate
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [SubTemplate]
 */
@Repository
interface SubTemplateRepository : ReactiveNeo4jRepository<SubTemplate<*, *, *>, String>