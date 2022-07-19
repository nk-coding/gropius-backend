package gropius.service.template

import gropius.model.template.IMSTemplate
import gropius.repository.template.IMSTemplateRepository
import org.springframework.stereotype.Service

/**
 * Service [IMSTemplate]s. Provides functions to create, update and delete
 *
 * @param repository the associated repository used for CRUD functionality
 */
@Service
class IMSTemplateService(repository: IMSTemplateRepository) : TemplateService<IMSTemplate>(repository)