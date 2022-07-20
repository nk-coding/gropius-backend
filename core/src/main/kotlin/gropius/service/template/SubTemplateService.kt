package gropius.service.template

import gropius.model.template.SubTemplate
import gropius.repository.template.SubTemplateRepository
import org.springframework.stereotype.Service

/**
 * Service [SubTemplate]s. Provides functions to create, update and delete
 *
 * @param repository the associated repository used for CRUD functionality
 */
@Service
class SubTemplateService(repository: SubTemplateRepository) :
    BaseTemplateService<SubTemplate<*, *, *>, SubTemplateRepository>(repository)