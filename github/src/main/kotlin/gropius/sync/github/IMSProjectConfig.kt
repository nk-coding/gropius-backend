package gropius.sync.github

import gropius.model.architecture.IMSProject

data class IMSProjectConfig(
    val imsProject: IMSProject,
    val imsConfig: IMSConfig,
    val botUser: String,
    val readUser: String,
    val repo: RepoDescription
) {
    constructor(
        helper: JsonHelper, imsConfig: IMSConfig, imsProject: IMSProject
    ) : this(
        imsProject, imsConfig,
        helper.parseString(imsProject.templatedFields["bot-user"]) ?: imsConfig.botUser,
        imsConfig.readUser,
        helper.objectMapper.readValue<RepoDescription>(
            imsProject.templatedFields["repo"]!!, RepoDescription::class.java
        )
    )
}
