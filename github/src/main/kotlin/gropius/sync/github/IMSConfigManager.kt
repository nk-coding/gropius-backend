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

/**
 * Find and ensure the IMSTemplate in the database
 * @param neoOperations Reference for the spring instance of ReactiveNeo4jOperations
 */
@Component
class IMSConfigManager(
    @Qualifier("graphglueNeo4jOperations")
    private val neoOperations: ReactiveNeo4jOperations,
) {

    companion object {
        /**
         * JSON schema version to include
         */
        private const val SCHEMA = "https://json-schema.org/draft/2020-12/schema"

        /**
         * Template fields for IMSTemplate and IMSProjectTemplate
         */
        private val COMMON_TEMPLATE_FIELDS = mapOf("bot-user" to obj {
            "\$schema" to Companion.SCHEMA
            "type" to arr["null", "string"]
        }.toString(), "last-notification" to obj {
            "\$schema" to Companion.SCHEMA
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
         * Name of the requested IMSTemplate
         */
        private const val IMS_TEMPLATE_NAME = "Github"

        /**
         * Fields of the requestes IMSTemplate
         */
        private val IMS_TEMPLATE_FIELDS = mapOf("read-user" to obj {
            "\$schema" to Companion.SCHEMA
            "type" to arr["null", obj {
                "type" to "string"
                "gropius-node" to "GropiusUser"
                "gropius-type" to "github-user"
            }]
        }.toString(), "graphql-url" to obj {
            "\$schema" to schema
            "type" to "string"
            "format" to "uri"
        }.toString()) + Companion.COMMON_TEMPLATE_FIELDS

        /**
         * Name of requested IMSProjectTemplate
         */
        private const val IMS_PROJECT_TEMPLATE_NAME = "Github"

        /**
         * Fields of the requestes IMSProjectTemplate
         */
        private val IMS_PROJECT_TEMPLATE_FIELDS = mapOf("repo" to obj {
            "\$schema" to Companion.SCHEMA
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
        }.toString()) + Companion.COMMON_TEMPLATE_FIELDS

        /**
         * Name of the ensured IMSIssueTemplate
         */
        private const val IMS_ISSUE_TEMPLATE_NAME = "Github Issue"

        /**
         * Fields of the required IMSIssueTemplate
         */
        private val IMS_ISSUE_TEMPLATE_FIELDS = mapOf<String, String>("last-notification" to obj {
            "\$schema" to Companion.SCHEMA
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
    }

    /**
     * Check if a given Template can be used as if it was the reference template.
     * @param possibleTemplate a random template
     * @param referenceName the title to check
     * @param referenceType check if all values specified by possibleTemplate are valid using the referenceType schemas
     * @return true if valid
     */
    private fun isContentCompatible(
        possibleTemplate: BaseTemplate<*, *>, referenceName: String, referenceType: Map<String, String>
    ): Boolean {
        //TODO: Replace with matching logic of template update operation
        return (possibleTemplate.name == referenceName) && (possibleTemplate.templateFieldSpecifications == referenceType)
    }

    /**
     * Find a matching set of IMSTemplate, IMSProjectTemplate and IMSIssueTemplate
     * @return the set of templates
     */
    suspend fun findTemplates(): Set<IMSTemplate> {
        val acceptableTemplates = neoOperations.findAll<IMSTemplate>().filter {
            (!it.isDeprecated) && isContentCompatible(
                it, Companion.IMS_TEMPLATE_NAME, Companion.IMS_TEMPLATE_FIELDS
            ) && isContentCompatible(
                it.imsProjectTemplate().value,
                Companion.IMS_PROJECT_TEMPLATE_NAME,
                Companion.IMS_PROJECT_TEMPLATE_FIELDS
            ) && isContentCompatible(
                it.imsIssueTemplate().value, Companion.IMS_ISSUE_TEMPLATE_NAME, Companion.IMS_ISSUE_TEMPLATE_FIELDS
            )
        }.toSet()
        if (acceptableTemplates.isEmpty()) {
            val imsTemplate =
                IMSTemplate(Companion.IMS_TEMPLATE_NAME, "", Companion.IMS_TEMPLATE_FIELDS.toMutableMap(), false)
            imsTemplate.imsProjectTemplate().value = IMSProjectTemplate(
                Companion.IMS_PROJECT_TEMPLATE_NAME, "", Companion.IMS_PROJECT_TEMPLATE_FIELDS.toMutableMap()
            )
            imsTemplate.imsIssueTemplate().value = IMSIssueTemplate(
                Companion.IMS_ISSUE_TEMPLATE_NAME, "", Companion.IMS_ISSUE_TEMPLATE_FIELDS.toMutableMap()
            )
            acceptableTemplates.plus(neoOperations.save(imsTemplate).awaitSingle())
        }
        return acceptableTemplates
    }

}
