package gropius.repository.issue

import gropius.model.issue.Artefact
import gropius.repository.GropiusRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [Artefact]
 */
@Repository
interface ArtefactRepository : GropiusRepository<Artefact, String>