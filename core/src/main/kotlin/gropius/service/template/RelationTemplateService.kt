package gropius.service.template

import gropius.model.template.RelationTemplate
import gropius.repository.template.RelationTemplateRepository
import org.springframework.stereotype.Service

/**
 * Service [RelationTemplate]s. Provides functions to create, update and delete
 *
 * @param repository the associated repository used for CRUD functionality
 */
@Service
class RelationTemplateService(repository: RelationTemplateRepository) : TemplateService<RelationTemplate>(repository)