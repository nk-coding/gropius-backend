package gropius.service.user.permission

import gropius.model.user.permission.ComponentPermission
import gropius.repository.user.permission.ComponentPermissionRepository
import org.springframework.stereotype.Service

/**
 * Service for [ComponentPermission]s. Provides functions to create, update and delete
 *
 * @param repository the associated repository used for CRUD functionality
 */
@Service
class ComponentPermissionService(repository: ComponentPermissionRepository) :
    TrackablePermissionService<ComponentPermission, ComponentPermissionRepository>(repository)