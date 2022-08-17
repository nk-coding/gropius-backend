package gropius.repository.issue.timeline

import gropius.model.issue.timeline.TemplateFieldChangedEvent
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [TemplateFieldChangedEvent]
 */
@Repository
interface TemplateFieldChangedEventRepository : ReactiveNeo4jRepository<TemplateFieldChangedEvent, String>