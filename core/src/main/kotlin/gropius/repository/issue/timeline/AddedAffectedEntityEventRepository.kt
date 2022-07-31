package gropius.repository.issue.timeline

import gropius.model.issue.timeline.AddedAffectedEntityEvent
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [AddedAffectedEntityEvent]
 */
@Repository
interface AddedAffectedEntityEventRepository : ReactiveNeo4jRepository<AddedAffectedEntityEvent, String>