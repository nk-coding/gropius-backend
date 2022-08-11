package gropius.repository.architecture

import gropius.model.architecture.IMSProject
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [IMSProject]
 */
@Repository
interface IMSProjectRepository : ReactiveNeo4jRepository<IMSProject, String>