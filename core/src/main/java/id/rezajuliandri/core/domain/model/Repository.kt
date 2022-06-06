package id.rezajuliandri.core.domain.model

data class Repository(
    val id: Int,
    val username: String,
    val htmlUrl: String,
    val name: String,
    val description: String? = null,
    val updatedAt: String
)