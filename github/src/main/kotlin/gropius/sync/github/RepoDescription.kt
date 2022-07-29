package gropius.sync.github

import com.fasterxml.jackson.annotation.JsonProperty

data class RepoDescription(
    @JsonProperty("owner")
    val owner: String,
    @JsonProperty("repo")
    val repo: String
)