package gropius.service.architecture

import gropius.model.architecture.Relation
import gropius.repository.architecture.RelationRepository
import gropius.service.common.ExtensibleNodeService
import org.springframework.stereotype.Service

/**
 * Service [Relation]s. Provides functions to create, update and delete
 *
 * @param repository the associated repository used for CRUD functionality
 */
@Service
class RelationService(repository: RelationRepository) : ExtensibleNodeService<Relation>(repository)