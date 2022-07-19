package gropius.service.template

import gropius.model.template.InterfaceSpecificationTemplate
import gropius.repository.template.InterfaceSpecificationTemplateRepository
import org.springframework.stereotype.Service

/**
 * Service [InterfaceSpecificationTemplate]s. Provides functions to create, update and delete
 *
 * @param repository the associated repository used for CRUD functionality
 */
@Service
class InterfaceSpecificationTemplateService(repository: InterfaceSpecificationTemplateRepository) : TemplateService<InterfaceSpecificationTemplate>(repository)