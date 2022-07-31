package gropius.repository.issue

import gropius.model.issue.Artefact
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [Artefact]
 */
@Repository
interface ArtefactRepository : ReactiveNeo4jRepository<Artefact, String>