package gropius.repository.architecture

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.architecture.IMS

/**
 * Repository for [IMS]
 */
@Repository
interface IMSRepository : ReactiveNeo4jRepository<IMS, String>