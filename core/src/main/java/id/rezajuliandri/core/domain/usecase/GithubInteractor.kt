package id.rezajuliandri.core.domain.usecase

import id.rezajuliandri.core.data.Resource
import id.rezajuliandri.core.domain.model.Repository
import id.rezajuliandri.core.domain.model.User
import id.rezajuliandri.core.domain.repository.IGithubRepository
import kotlinx.coroutines.flow.Flow

class GithubInteractor(private val repository: IGithubRepository) : GithubUseCase {
    override fun searchUser(username: String): Flow<Resource<List<User>>> =
        repository.searchUser(username)

    override fun getDetailUser(username: String): Flow<Resource<User?>> =
        repository.getDetailUser(username)

    override fun getRepository(username: String): Flow<Resource<List<Repository>>> =
        repository.getRepository(username)

    override fun getFoll(username: String, type: String): Flow<Resource<List<User>>> =
        repository.getFoll(username, type)

    override fun isFavorite(username: String): Flow<Boolean> =
        repository.isFavorite(username)

    override suspend fun setFavorite(username: String, state: Boolean) =
        repository.setFavorite(username, state)

    override fun getFavorites(): Flow<List<User>> =
        repository.getFavorites()


}