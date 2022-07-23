package gropius.service.architecture

import gropius.model.architecture.IMS
import gropius.repository.architecture.IMSRepository
import gropius.service.common.NamedNodeService
import org.springframework.stereotype.Service

/**
 * Service for [IMS]s. Provides functions to create, update and delete
 *
 * @param repository the associated repository used for CRUD functionality
 */
@Service
class IMSService(repository: IMSRepository) : NamedNodeService<IMS, IMSRepository>(repository)