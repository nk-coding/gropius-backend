package gropius.service.architecture

import gropius.model.architecture.InterfaceSpecificationVersion
import gropius.repository.architecture.InterfaceSpecificationVersionRepository
import org.springframework.stereotype.Service

/**
 * Service for [InterfaceSpecificationVersion]s. Provides functions to create, update and delete
 *
 * @param repository the associated repository used for CRUD functionality
 */
@Service
class InterfaceSpecificationVersionService(repository: InterfaceSpecificationVersionRepository) :
    ServiceEffectSpecificationLocationService<InterfaceSpecificationVersion, InterfaceSpecificationVersionRepository>(
        repository
    )