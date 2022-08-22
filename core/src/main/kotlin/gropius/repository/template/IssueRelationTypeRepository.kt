package gropius.repository.template

import gropius.model.template.IssueRelationType
import gropius.repository.GropiusRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [IssueRelationType]
 */
@Repository
interface IssueRelationTypeRepository : GropiusRepository<IssueRelationType, String>