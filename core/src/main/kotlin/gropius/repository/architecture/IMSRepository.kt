package gropius.repository.architecture

import gropius.model.architecture.IMS
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [IMS]
 */
@Repository
interface IMSRepository : ReactiveNeo4jRepository<IMS, String>