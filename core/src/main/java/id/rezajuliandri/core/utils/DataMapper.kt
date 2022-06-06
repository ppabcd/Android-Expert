package id.rezajuliandri.core.utils

import id.rezajuliandri.core.data.local.entity.RepositoryEntity
import id.rezajuliandri.core.data.local.entity.UserEntity
import id.rezajuliandri.core.data.remote.responses.RepositoryResponse
import id.rezajuliandri.core.data.remote.responses.UserResponse
import id.rezajuliandri.core.domain.model.Repository
import id.rezajuliandri.core.domain.model.User

object DataMapper {
    fun mapResponsesToEntities(input: List<UserResponse>): List<UserEntity> {
        val userList = ArrayList<UserEntity>()
        input.map {
            val user = UserEntity(
                id = it.id,
                login = it.login,
                name = it.name,
                avatarUrl = it.avatarUrl,
                followers = it.followers,
                following = it.following,
                company = it.company,
                location = it.location,
                publicRepos = it.publicRepos,
            )
            userList.add(user)
        }
        return userList
    }

    fun mapEntitiesToDomain(input: List<UserEntity>): List<User> =
        input.map {
            User(
                id = it.id,
                username = it.login,
                name = it.name,
                avatarUrl = it.avatarUrl,
                followers = it.followers,
                following = it.following,
                company = it.company,
                location = it.location,
                publicRepos = it.publicRepos,
            )
        }

    fun mapEntityToDomain(input: UserEntity): User = User(
        id = input.id,
        username = input.login,
        name = input.name,
        avatarUrl = input.avatarUrl,
        followers = input.followers,
        following = input.following,
        company = input.company,
        location = input.location,
        publicRepos = input.publicRepos,
        isFavorite = input.isFavorite
    )

    fun mapResponsesToEntity(input: UserResponse): UserEntity = UserEntity(
        id = input.id,
        login = input.login,
        name = input.name,
        avatarUrl = input.avatarUrl,
        followers = input.followers,
        following = input.following,
        company = input.company,
        location = input.location,
        publicRepos = input.publicRepos,
    )

    fun mapResponsesToEntitiesRepo(
        input: List<RepositoryResponse>,
        username: String
    ): List<RepositoryEntity> {
        val repositoryList = ArrayList<RepositoryEntity>()
        input.map {
            val repository = RepositoryEntity(
                id = it.id,
                name = it.name,
                username = username,
                htmlUrl = it.htmlUrl,
                description = it.description,
                updatedAt = it.updatedAt
            )
            repositoryList.add(repository)
        }
        return repositoryList
    }

    fun mapEntitiesToDomainRepo(input: List<RepositoryEntity>, username: String): List<Repository> =
        input.map {
            Repository(
                id = it.id,
                name = it.name,
                username = username,
                htmlUrl = it.htmlUrl,
                description = it.description,
                updatedAt = it.updatedAt
            )
        }
}