package gropius.repository.template

import gropius.model.template.Template
import gropius.repository.GropiusRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [Template]
 */
@Repository
interface TemplateRepository : GropiusRepository<Template<*, *>, String>