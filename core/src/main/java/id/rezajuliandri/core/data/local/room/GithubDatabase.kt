package id.rezajuliandri.core.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import id.rezajuliandri.core.data.local.entity.RepositoryEntity
import id.rezajuliandri.core.data.local.entity.UserEntity

@Database(entities = [UserEntity::class, RepositoryEntity::class], version = 1, exportSchema = false)
abstract class GithubDatabase : RoomDatabase() {
    abstract fun githubDao(): GithubDao
}