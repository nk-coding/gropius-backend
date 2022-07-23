package gropius.service.architecture

import gropius.model.architecture.InterfacePart
import gropius.repository.architecture.InterfacePartRepository
import org.springframework.stereotype.Service

/**
 * Service for [InterfacePart]s. Provides functions to create, update and delete
 *
 * @param repository the associated repository used for CRUD functionality
 */
@Service
class InterfacePartService(repository: InterfacePartRepository) :
    ServiceEffectSpecificationLocationService<InterfacePart, InterfacePartRepository>(repository)