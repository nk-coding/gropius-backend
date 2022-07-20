package gropius.service.template

import gropius.model.template.ArtefactTemplate
import gropius.repository.template.ArtefactTemplateRepository
import org.springframework.stereotype.Service

/**
 * Service [ArtefactTemplate]s. Provides functions to create, update and delete
 *
 * @param repository the associated repository used for CRUD functionality
 */
@Service
class ArtefactTemplateService(repository: ArtefactTemplateRepository) :
    AbstractTemplateService<ArtefactTemplate, ArtefactTemplateRepository>(repository)