package gropius.sync.github

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Object describing the identification for a github repo
 * @param owner first part of url path part
 * @param repo second part of url path part
 */
data class RepoDescription(
    @JsonProperty("owner")
    val owner: String,
    @JsonProperty("repo")
    val repo: String
)