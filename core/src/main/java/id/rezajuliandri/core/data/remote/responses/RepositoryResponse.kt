package id.rezajuliandri.core.data.remote.responses

import com.google.gson.annotations.SerializedName

data class RepositoryResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("username")
    val username: String,
    @SerializedName("html_url")
    val htmlUrl: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("updated_at")
    val updatedAt: String,
)