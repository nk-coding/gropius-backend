package gropius.sync.github

import com.lectra.koson.arr
import com.lectra.koson.obj
import gropius.model.template.BaseTemplate
import gropius.model.template.IMSIssueTemplate
import gropius.model.template.IMSProjectTemplate
import gropius.model.template.IMSTemplate
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.toSet
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.neo4j.core.ReactiveNeo4jOperations
import org.springframework.data.neo4j.core.findAll
import org.springframework.stereotype.Component

@Component
/**
 * @param neoOperations Reference for the spring instance of ReactiveNeo4jOperations
 */
class IMSConfigManager(
    @Qualifier("graphglueNeo4jOperations")
    private val neoOperations: ReactiveNeo4jOperations,
) {
    private val schema = "https://json-schema.org/draft/2020-12/schema"

    private val commonTemplateFields = mapOf("bot-user" to obj {
        "\$schema" to schema
        "type" to arr["null", "string"]
    }.toString(), "last-notification" to obj {
        "\$schema" to schema
        "type" to arr["null", obj {
            "type" to "object"
            "properties" to obj {
                "title" to obj {
                    "type" to "string"
                }
                "content" to obj {
                    "type" to "string"
                }
            }
            "required" to arr["title", "content"]
            "gropius-type" to "notification"
        }]
    }.toString())
    private val imsTemplateName = "Github"
    private val imsTemplateFields = mapOf("read-user" to obj {
        "\$schema" to schema
        "type" to arr["null", obj {
            "type" to "string"
            "gropius-node" to "IMSUser"
            "gropius-type" to "github-user"
        }]
    }.toString()) + commonTemplateFields
    private val imsProjectTemplateName = "Github"
    private val imsProjectTemplateFields = mapOf("repo" to obj {
        "\$schema" to schema
        "type" to "object"
        "properties" to obj {
            "owner" to obj {
                "type" to "string"
            }
            "repo" to obj {
                "type" to "string"
            }
        }
        "required" to arr["owner", "repo"]
        "gropius-type" to "github-owner"
    }.toString()) + commonTemplateFields
    private val imsIssueTemplateName = "Github Issue"
    private val imsIssueTemplateFields = mapOf<String, String>("last-notification" to obj {
        "\$schema" to schema
        "type" to arr["null", obj {
            "type" to "object"
            "properties" to obj {
                "title" to obj {
                    "type" to "string"
                }
                "content" to obj {
                    "type" to "string"
                }
            }
            "required" to arr["title", "content"]
            "gropius-type" to "notification"
        }]
    }.toString())

    /**
     * Check if a given Template can be used as if it was the reference template.
     * @param possibleTemplate a random template
     * @param referenceName The title to check
     * @param referenceType Check if all values specified by possibleTemplate are valid using the referenceType schemas
     * @return True if valid
     */
    private fun isContentCompatible(
        possibleTemplate: BaseTemplate<*, *>, referenceName: String, referenceType: Map<String, String>
    ): Boolean {
        //TODO: Replace with matching logic of template update operation
        return (possibleTemplate.name == referenceName) && (possibleTemplate.templateFieldSpecifications == referenceType)
    }

    suspend fun findTemplates(): Set<IMSTemplate> {
        val acceptableTemplates = neoOperations.findAll<IMSTemplate>().filter {
            (!it.isDeprecated) && isContentCompatible(
                it, imsTemplateName, imsTemplateFields
            ) && isContentCompatible(
                it.imsProjectTemplate().value, imsProjectTemplateName, imsProjectTemplateFields
            ) && isContentCompatible(
                it.imsIssueTemplate().value, imsIssueTemplateName, imsIssueTemplateFields
            )
        }.toSet()
        if (acceptableTemplates.isEmpty()) {
            val imsTemplate = IMSTemplate(imsTemplateName, "", imsTemplateFields.toMutableMap(), false)
            imsTemplate.imsProjectTemplate().value =
                IMSProjectTemplate(imsProjectTemplateName, "", imsProjectTemplateFields.toMutableMap())
            imsTemplate.imsIssueTemplate().value =
                IMSIssueTemplate(imsIssueTemplateName, "", imsIssueTemplateFields.toMutableMap())
            acceptableTemplates.plus(neoOperations.save(imsTemplate).awaitSingle())
        }
        return acceptableTemplates
    }
}