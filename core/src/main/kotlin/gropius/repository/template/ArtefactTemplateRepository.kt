package gropius.repository.template

import gropius.model.template.ArtefactTemplate
import gropius.repository.GropiusRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [ArtefactTemplate]
 */
@Repository
interface ArtefactTemplateRepository : GropiusRepository<ArtefactTemplate, String>