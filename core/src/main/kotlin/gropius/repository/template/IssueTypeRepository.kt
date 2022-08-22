package gropius.repository.template

import gropius.model.template.IssueType
import gropius.repository.GropiusRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [IssueType]
 */
@Repository
interface IssueTypeRepository : GropiusRepository<IssueType, String>