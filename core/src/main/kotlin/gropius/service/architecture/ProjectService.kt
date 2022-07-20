package gropius.service.architecture

import gropius.model.architecture.Project
import gropius.repository.architecture.ProjectRepository
import org.springframework.stereotype.Service

/**
 * Service [Project]s. Provides functions to create, update and delete
 *
 * @param repository the associated repository used for CRUD functionality
 */
@Service
class ProjectService(repository: ProjectRepository) : TrackableService<Project, ProjectRepository>(repository)