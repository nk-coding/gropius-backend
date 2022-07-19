package gropius.repository.issue

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.issue.Artefact

/**
 * Repository for [Artefact]
 */
@Repository
interface ArtefactRepository : ReactiveNeo4jRepository<Artefact, String>