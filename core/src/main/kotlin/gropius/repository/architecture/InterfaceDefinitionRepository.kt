package gropius.repository.architecture

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.architecture.InterfaceDefinition

/**
 * Repository for [InterfaceDefinition]
 */
@Repository
interface InterfaceDefinitionRepository : ReactiveNeo4jRepository<InterfaceDefinition, String>