package gropius.service.architecture

import gropius.model.architecture.ServiceEffectSpecification
import gropius.repository.architecture.ServiceEffectSpecificationRepository
import gropius.service.common.NamedNodeService
import org.springframework.stereotype.Service

/**
 * Service for [ServiceEffectSpecification]s. Provides functions to create, update and delete
 *
 * @param repository the associated repository used for CRUD functionality
 */
@Service
class ServiceEffectSpecificationService(repository: ServiceEffectSpecificationRepository) :
    NamedNodeService<ServiceEffectSpecification, ServiceEffectSpecificationRepository>(repository)