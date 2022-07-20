package gropius.service.architecture

import gropius.model.architecture.Component
import gropius.repository.architecture.ComponentRepository
import org.springframework.stereotype.Service

/**
 * Service [Component]s. Provides functions to create, update and delete
 *
 * @param repository the associated repository used for CRUD functionality
 */
@Service
class ComponentService(repository: ComponentRepository) : TrackableService<Component, ComponentRepository>(repository)