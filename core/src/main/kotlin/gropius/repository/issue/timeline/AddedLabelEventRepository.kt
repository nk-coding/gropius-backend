package gropius.repository.issue.timeline

import gropius.model.issue.timeline.AddedLabelEvent
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [AddedLabelEvent]
 */
@Repository
interface AddedLabelEventRepository : ReactiveNeo4jRepository<AddedLabelEvent, String>