package gropius.service.template

import gropius.model.template.IssueTemplate
import gropius.repository.template.IssueTemplateRepository
import org.springframework.stereotype.Service

/**
 * Service [IssueTemplate]s. Provides functions to create, update and delete
 *
 * @param repository the associated repository used for CRUD functionality
 */
@Service
class IssueTemplateService(repository: IssueTemplateRepository) :
    AbstractTemplateService<IssueTemplate, IssueTemplateRepository>(repository)