package gropius.repository.architecture

import gropius.model.architecture.Project
import gropius.repository.GropiusRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [Project]
 */
@Repository
interface ProjectRepository : GropiusRepository<Project, String>