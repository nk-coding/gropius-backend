package gropius.repository.template

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.template.SubTemplate

/**
 * Repository for [SubTemplate]
 */
@Repository
interface SubTemplateRepository : ReactiveNeo4jRepository<SubTemplate<*, *, *>, String>