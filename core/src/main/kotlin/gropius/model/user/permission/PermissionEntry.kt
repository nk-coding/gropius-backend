package gropius.model.user.permission

/**
 * Entry for a [BasePermission].
 * Used to build the GraphQL enum dynamically based on the [PermissionEntry] provided via
 * [PermissionEntryCollection] beans.
 *
 * @param name the name of the enum entry
 * @param description the description of the enum entry
 */
class PermissionEntry(val name: String, val description: String)