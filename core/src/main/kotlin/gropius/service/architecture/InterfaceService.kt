package gropius.service.architecture

import gropius.model.architecture.Interface
import gropius.repository.architecture.InterfaceRepository
import org.springframework.stereotype.Service

/**
 * Service for [Interface]s. Provides functions to create, update and delete
 *
 * @param repository the associated repository used for CRUD functionality
 */
@Service
class InterfaceService(repository: InterfaceRepository) :
    RelationPartnerService<Interface, InterfaceRepository>(repository)