package gropius.repository.template

import gropius.model.template.RelationPartnerTemplate
import gropius.repository.GropiusRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [RelationPartnerTemplate]
 */
@Repository
interface RelationPartnerTemplateRepository : GropiusRepository<RelationPartnerTemplate<*, *>, String>