package gropius.repository.architecture

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.architecture.IMSProject

/**
 * Repository for [IMSProject]
 */
@Repository
interface IMSProjectRepository : ReactiveNeo4jRepository<IMSProject, String>