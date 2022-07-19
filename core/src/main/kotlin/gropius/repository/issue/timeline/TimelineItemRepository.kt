package gropius.repository.issue.timeline

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.issue.timeline.TimelineItem

/**
 * Repository for [TimelineItem]
 */
@Repository
interface TimelineItemRepository : ReactiveNeo4jRepository<TimelineItem, String>