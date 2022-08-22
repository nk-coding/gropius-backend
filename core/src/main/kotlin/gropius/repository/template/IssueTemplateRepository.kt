package gropius.repository.template

import gropius.model.template.IssueTemplate
import gropius.repository.GropiusRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [IssueTemplate]
 */
@Repository
interface IssueTemplateRepository : GropiusRepository<IssueTemplate, String>