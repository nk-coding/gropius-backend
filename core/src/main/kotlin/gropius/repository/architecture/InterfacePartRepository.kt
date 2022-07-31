package gropius.repository.architecture

import gropius.model.architecture.InterfacePart
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [InterfacePart]
 */
@Repository
interface InterfacePartRepository : ReactiveNeo4jRepository<InterfacePart, String>