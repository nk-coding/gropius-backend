package gropius.repository.architecture

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.architecture.Interface

/**
 * Repository for [Interface]
 */
@Repository
interface InterfaceRepository : ReactiveNeo4jRepository<Interface, String>