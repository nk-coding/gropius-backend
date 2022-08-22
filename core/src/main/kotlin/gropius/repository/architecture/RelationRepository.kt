package gropius.repository.architecture

import gropius.model.architecture.Relation
import gropius.repository.GropiusRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [Relation]
 */
@Repository
interface RelationRepository : GropiusRepository<Relation, String>