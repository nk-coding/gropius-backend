package gropius.repository.architecture

import gropius.model.architecture.InterfaceDefinition
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [InterfaceDefinition]
 */
@Repository
interface InterfaceDefinitionRepository : ReactiveNeo4jRepository<InterfaceDefinition, String>