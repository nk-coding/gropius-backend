package gropius.repository.template

import gropius.model.template.RelationTemplate
import gropius.repository.GropiusRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [RelationTemplate]
 */
@Repository
interface RelationTemplateRepository : GropiusRepository<RelationTemplate, String>