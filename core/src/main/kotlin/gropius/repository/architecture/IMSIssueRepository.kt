package gropius.repository.architecture

import gropius.model.architecture.IMSIssue
import gropius.repository.GropiusRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [IMSIssue]
 */
@Repository
interface IMSIssueRepository : GropiusRepository<IMSIssue, String>