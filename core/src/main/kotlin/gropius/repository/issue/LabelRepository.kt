package gropius.repository.issue

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.issue.Label

/**
 * Repository for [Label]
 */
@Repository
interface LabelRepository : ReactiveNeo4jRepository<Label, String>