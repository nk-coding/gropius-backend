package gropius.sync.github

import gropius.model.architecture.IMS

/**
 * Config read out from a single IMS node
 * @param ims the gropius ims to use as input
 * @param botUser the bot user string extracted from the template
 * @param readUser the read user string extracted from the template
 */
data class IMSConfig(
    val ims: IMS, val botUser: String, val readUser: String
) {
    /**
     * @param ims the gropius ims to use as input
     * @param helper Reference for the spring instance of JsonHelper
     */
    constructor(
        helper: JsonHelper, ims: IMS
    ) : this(
        ims,
        helper.parseString(ims.templatedFields["bot-user"]) ?: "github-bot",
        helper.parseString(ims.templatedFields["read-user"])!!
    )
}