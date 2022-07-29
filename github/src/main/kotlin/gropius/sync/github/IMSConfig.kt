package gropius.sync.github

import gropius.model.architecture.IMS

data class IMSConfig(
    val ims: IMS, val botUser: String, val readUser: String
) {
    constructor(
        helper: JsonHelper, ims: IMS
    ) : this(
        ims,
        helper.parseString(ims.templatedFields["bot-user"]) ?: "github-bot",
        helper.parseString(ims.templatedFields["read-user"])!!
    )
}
