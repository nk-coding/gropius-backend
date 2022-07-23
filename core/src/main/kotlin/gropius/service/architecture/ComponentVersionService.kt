package gropius.service.architecture

import gropius.model.architecture.ComponentVersion
import gropius.repository.architecture.ComponentVersionRepository
import org.springframework.stereotype.Service

/**
 * Service for [ComponentVersion]s. Provides functions to create, update and delete
 *
 * @param repository the associated repository used for CRUD functionality
 */
@Service
class ComponentVersionService(repository: ComponentVersionRepository) :
    RelationPartnerService<ComponentVersion, ComponentVersionRepository>(repository)