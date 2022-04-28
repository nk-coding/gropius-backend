package gropius.model.common

import io.github.graphglue.model.DomainNode
import java.time.LocalDateTime

@DomainNode
abstract class NamedSyncNode(
    createdAt: LocalDateTime, lastModifiedAt: LocalDateTime, override var name: String, override var description: String
) : SyncNode(createdAt, lastModifiedAt), Named