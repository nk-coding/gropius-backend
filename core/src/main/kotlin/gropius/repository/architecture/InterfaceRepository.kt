package gropius.repository.architecture

import gropius.model.architecture.Interface
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [Interface]
 */
@Repository
interface InterfaceRepository : ReactiveNeo4jRepository<Interface, String>