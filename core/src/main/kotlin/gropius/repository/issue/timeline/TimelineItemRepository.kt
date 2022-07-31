package gropius.repository.issue.timeline

import gropius.model.issue.timeline.TimelineItem
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [TimelineItem]
 */
@Repository
interface TimelineItemRepository : ReactiveNeo4jRepository<TimelineItem, String>