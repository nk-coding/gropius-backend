package gropius.repository.issue.timeline

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.issue.timeline.AddedAffectedEntityEvent

/**
 * Repository for [AddedAffectedEntityEvent]
 */
@Repository
interface AddedAffectedEntityEventRepository : ReactiveNeo4jRepository<AddedAffectedEntityEvent, String>