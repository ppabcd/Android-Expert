package id.rezajuliandri.core.data.local.room

import androidx.room.*
import id.rezajuliandri.core.data.local.entity.RepositoryEntity
import id.rezajuliandri.core.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GithubDao {
    @Query("SELECT * FROM users WHERE username like '%' || :name || '%'")
    fun search(name: String): Flow<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(userList: List<UserEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(userList: UserEntity)

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    fun getUser(username: String): Flow<UserEntity?>

    @Query("SELECT * FROM repositories WHERE username = :username")
    fun getRepository(username: String): Flow<List<RepositoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRepository(repositoryList: List<RepositoryEntity>)

    @Query("SELECT EXISTS(SELECT * FROM users WHERE is_favorite = 1 AND username = :username) ")
    fun isFavorite(username: String): Flow<Boolean>

    @Update
    suspend fun updateUser(userEntity: UserEntity)

    @Query("UPDATE users SET is_favorite = :state WHERE username = :username")
    suspend fun updateFavorite(username: String, state: Boolean)

    @Query("SELECT * FROM users WHERE is_favorite = 1")
    suspend fun getFavorites(): List<UserEntity>
}