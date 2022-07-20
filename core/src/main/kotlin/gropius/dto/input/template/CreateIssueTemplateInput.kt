package gropius.dto.input.template

import com.expediagroup.graphql.generator.annotations.GraphQLDescription

@GraphQLDescription("Input for the createIssueTemplate mutation.")
class CreateIssueTemplateInput(
    @GraphQLDescription("Set of all types Issues with the created Template can have.")
    val issueTypes: List<IssueTypeInput>,
    @GraphQLDescription("Set of all types Assignments to Issues with the created can have.")
    val assignmentTypes: List<AssignmentTypeInput>,
    @GraphQLDescription("Set of all priorities Issues with the created can have.")
    val issuePriorities: List<IssuePriorityInput>,
    @GraphQLDescription("Set of all types outgoing IssueRelations of Issues with the created can have")
    val relationTypes: List<IssueRelationTypeInput>
) : CreateTemplateInput()