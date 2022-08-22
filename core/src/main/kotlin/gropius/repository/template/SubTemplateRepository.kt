package gropius.repository.template

import gropius.model.template.SubTemplate
import gropius.repository.GropiusRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [SubTemplate]
 */
@Repository
interface SubTemplateRepository : GropiusRepository<SubTemplate<*, *, *>, String>