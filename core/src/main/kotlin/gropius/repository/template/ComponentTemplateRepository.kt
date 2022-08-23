package gropius.repository.template

import gropius.model.template.ComponentTemplate
import gropius.repository.GropiusRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [ComponentTemplate]
 */
@Repository
interface ComponentTemplateRepository : GropiusRepository<ComponentTemplate, String>