package gropius.model.issue.timeline

import gropius.model.common.SyncNode
import gropius.model.issue.Issue
import io.github.graphglue.model.Direction
import io.github.graphglue.model.DomainNode
import io.github.graphglue.model.FilterProperty
import io.github.graphglue.model.NodeRelationship
import org.springframework.data.annotation.Transient
import java.time.OffsetDateTime


@DomainNode
abstract class TimelineItem(createdAt: OffsetDateTime, lastModifiedAt: OffsetDateTime) :
    SyncNode(createdAt, lastModifiedAt) {

    @NodeRelationship(Issue.TIMELINE, Direction.INCOMING)
    @FilterProperty
    @delegate:Transient
    var issue by NodeProperty<Issue>()

}