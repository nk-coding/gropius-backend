package gropius.service.template

import gropius.model.template.TemplatedNode
import gropius.repository.template.TemplatedNodeRepository
import org.springframework.stereotype.Service

/**
 * Service [TemplatedNode]s. Provides functions to create, update and delete
 *
 * @param repository the associated repository used for CRUD functionality
 */
@Service
class TemplatedNodeService(val repository: TemplatedNodeRepository)