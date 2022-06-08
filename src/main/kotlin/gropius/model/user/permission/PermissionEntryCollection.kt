package gropius.model.user.permission

/**
 * Wrapper for a set of [PermissionEntry]s for a specific [BasePermission]
 * Used to determine the set of [PermissionEntry]s via dependency injection.
 * To provide additional [PermissionEntry] for a specific [BasePermission], just provide a bean with this class.
 * All beans are automatically detected and then merged.
 *
 * @param entries the set of [PermissionEntry]s
 * @param T the type of [BasePermission] for which the [PermissionEntry]s should be added
 */
class PermissionEntryCollection<T : BasePermission>(val entries: Set<PermissionEntry>)