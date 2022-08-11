package gropius.repository.issue

import gropius.model.issue.Label
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [Label]
 */
@Repository
interface LabelRepository : ReactiveNeo4jRepository<Label, String>