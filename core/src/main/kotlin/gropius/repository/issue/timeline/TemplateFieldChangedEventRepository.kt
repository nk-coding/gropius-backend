package gropius.repository.issue.timeline

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.issue.timeline.TemplateFieldChangedEvent

/**
 * Repository for [TemplateFieldChangedEvent]
 */
@Repository
interface TemplateFieldChangedEventRepository : ReactiveNeo4jRepository<TemplateFieldChangedEvent, String>