package gropius.service.architecture

import gropius.model.architecture.IMSProject
import gropius.repository.architecture.IMSProjectRepository
import gropius.service.common.AbstractExtensibleNodeService
import org.springframework.stereotype.Service

/**
 * Service [IMSProject]s. Provides functions to create, update and delete
 *
 * @param repository the associated repository used for CRUD functionality
 */
@Service
class IMSProjectService(repository: IMSProjectRepository) :
    AbstractExtensibleNodeService<IMSProject, IMSProjectRepository>(repository)