package gropius.repository.architecture

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.architecture.InterfacePart

/**
 * Repository for [InterfacePart]
 */
@Repository
interface InterfacePartRepository : ReactiveNeo4jRepository<InterfacePart, String>