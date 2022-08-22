package gropius.repository.architecture

import gropius.model.architecture.AffectedByIssue
import gropius.repository.GropiusRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [AffectedByIssue]
 */
@Repository
interface AffectedByIssueRepository : GropiusRepository<AffectedByIssue, String>