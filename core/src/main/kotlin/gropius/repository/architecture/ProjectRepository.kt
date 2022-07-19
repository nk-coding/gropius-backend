package gropius.repository.architecture

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.architecture.Project

/**
 * Repository for [Project]
 */
@Repository
interface ProjectRepository : ReactiveNeo4jRepository<Project, String>