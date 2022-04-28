package gropius.model.common

import io.github.graphglue.model.DomainNode
import java.time.OffsetDateTime

@DomainNode
abstract class NamedSyncNode(
    createdAt: OffsetDateTime, lastModifiedAt: OffsetDateTime, override var name: String, override var description: String
) : SyncNode(createdAt, lastModifiedAt), Named