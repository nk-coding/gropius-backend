package gropius.repository.template

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.template.ArtefactTemplate

/**
 * Repository for [ArtefactTemplate]
 */
@Repository
interface ArtefactTemplateRepository : ReactiveNeo4jRepository<ArtefactTemplate, String>