package id.rezajuliandri.core.data.local

import id.rezajuliandri.core.data.local.entity.RepositoryEntity
import id.rezajuliandri.core.data.local.entity.UserEntity
import id.rezajuliandri.core.data.local.room.GithubDao
import kotlinx.coroutines.flow.Flow

class LocalDataSource(private val githubDao: GithubDao) {
    fun search(name: String): Flow<List<UserEntity>> = githubDao.search(name)
    suspend fun insertUser(userList: List<UserEntity>) = githubDao.insertUser(userList)
    suspend fun insertUser(userList: UserEntity) = githubDao.insertUser(userList)
    fun getUser(username: String): Flow<UserEntity?> = githubDao.getUser(username)
    fun getRepository(username: String): Flow<List<RepositoryEntity>> =
        githubDao.getRepository(username)

    suspend fun insertRepository(repositoryList: List<RepositoryEntity>) =
        githubDao.insertRepository(repositoryList)

    fun isFavorite(username: String): Flow<Boolean> = githubDao.isFavorite(username)
    suspend fun updateFavorite(username: String, state: Boolean) =
        githubDao.updateFavorite(username, state)

    suspend fun getFavorites(): List<UserEntity> = githubDao.getFavorites()
}