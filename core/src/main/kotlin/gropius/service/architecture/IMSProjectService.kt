package gropius.service.architecture

import gropius.model.architecture.IMSProject
import gropius.repository.architecture.IMSProjectRepository
import gropius.service.common.ExtensibleNodeService
import org.springframework.stereotype.Service

/**
 * Service [IMSProject]s. Provides functions to create, update and delete
 *
 * @param repository the associated repository used for CRUD functionality
 */
@Service
class IMSProjectService(repository: IMSProjectRepository) : ExtensibleNodeService<IMSProject>(repository)