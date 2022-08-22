package gropius.repository.template

import gropius.model.template.BaseTemplate
import gropius.repository.GropiusRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [BaseTemplate]
 */
@Repository
interface BaseTemplateRepository : GropiusRepository<BaseTemplate<*, *>, String>