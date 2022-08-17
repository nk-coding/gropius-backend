package gropius.repository.template

import gropius.model.template.ArtefactTemplate
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [ArtefactTemplate]
 */
@Repository
interface ArtefactTemplateRepository : ReactiveNeo4jRepository<ArtefactTemplate, String>