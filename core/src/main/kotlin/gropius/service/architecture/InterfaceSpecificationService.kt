package gropius.service.architecture

import gropius.model.architecture.InterfaceSpecification
import gropius.repository.architecture.InterfaceSpecificationRepository
import org.springframework.stereotype.Service

/**
 * Service [InterfaceSpecification]s. Provides functions to create, update and delete
 *
 * @param repository the associated repository used for CRUD functionality
 */
@Service
class InterfaceSpecificationService(repository: InterfaceSpecificationRepository) : ServiceEffectSpecificationLocationService<InterfaceSpecification>(repository)