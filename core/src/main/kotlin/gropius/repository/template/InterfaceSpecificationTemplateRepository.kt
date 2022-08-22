package gropius.repository.template

import gropius.model.template.InterfaceSpecificationTemplate
import gropius.repository.GropiusRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [InterfaceSpecificationTemplate]
 */
@Repository
interface InterfaceSpecificationTemplateRepository : GropiusRepository<InterfaceSpecificationTemplate, String>