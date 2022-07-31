package gropius.repository.architecture

import gropius.model.architecture.Project
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [Project]
 */
@Repository
interface ProjectRepository : ReactiveNeo4jRepository<Project, String>