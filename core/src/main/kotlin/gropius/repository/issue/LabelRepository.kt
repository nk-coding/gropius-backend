package gropius.repository.issue

import gropius.model.issue.Label
import gropius.repository.GropiusRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [Label]
 */
@Repository
interface LabelRepository : GropiusRepository<Label, String>