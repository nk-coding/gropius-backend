package gropius.repository.issue.timeline

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.issue.timeline.AddedLabelEvent

/**
 * Repository for [AddedLabelEvent]
 */
@Repository
interface AddedLabelEventRepository : ReactiveNeo4jRepository<AddedLabelEvent, String>