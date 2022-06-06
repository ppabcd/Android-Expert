package id.rezajuliandri.core.domain.usecase

import id.rezajuliandri.core.data.Resource
import id.rezajuliandri.core.domain.model.Repository
import id.rezajuliandri.core.domain.model.User
import kotlinx.coroutines.flow.Flow

interface GithubUseCase {
    fun searchUser(username: String): Flow<Resource<List<User>>>
    fun getDetailUser(username: String): Flow<Resource<User?>>
    fun getRepository(username: String): Flow<Resource<List<Repository>>>
    fun getFoll(username: String, type: String): Flow<Resource<List<User>>>
    fun isFavorite(username: String): Flow<Boolean>
    suspend fun setFavorite(username: String, state: Boolean)
    fun getFavorites(): Flow<List<User>>
}