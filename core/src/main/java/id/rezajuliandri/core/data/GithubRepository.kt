package id.rezajuliandri.core.data

import id.rezajuliandri.core.data.local.LocalDataSource
import id.rezajuliandri.core.data.remote.RemoteDataSource
import id.rezajuliandri.core.data.remote.network.ApiResponse
import id.rezajuliandri.core.data.remote.responses.RepositoryResponse
import id.rezajuliandri.core.data.remote.responses.UserResponse
import id.rezajuliandri.core.domain.model.Repository
import id.rezajuliandri.core.domain.model.User
import id.rezajuliandri.core.domain.repository.IGithubRepository
import id.rezajuliandri.core.utils.DataMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

class GithubRepository constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) : IGithubRepository {
    override fun searchUser(username: String): Flow<Resource<List<User>>> =
        object : NetworkBoundResource<List<User>, List<UserResponse>>() {
            override fun loadFromDB(): Flow<List<User>> {
                return localDataSource.search(username).map { userEntity ->
                    DataMapper.mapEntitiesToDomain(userEntity)
                }
            }

            override fun shouldFetch(data: List<User>?): Boolean = true

            override suspend fun createCall(): Flow<ApiResponse<List<UserResponse>>> =
                remoteDataSource.search(username)

            override suspend fun saveCallResult(data: List<UserResponse>) {
                val userList = DataMapper.mapResponsesToEntities(data)
                localDataSource.insertUser(userList)
            }
        }.asFlow()

    override fun getDetailUser(username: String): Flow<Resource<User?>> =
        object : NetworkBoundResource<User?, UserResponse>() {
            override fun loadFromDB(): Flow<User?> {
                return localDataSource.getUser(username).map {
                    it?.let {
                        DataMapper.mapEntityToDomain(it)
                    }
                }
            }

            override fun shouldFetch(data: User?): Boolean = data?.isFavorite == false

            override suspend fun createCall(): Flow<ApiResponse<UserResponse>> =
                remoteDataSource.getDetail(username)


            override suspend fun saveCallResult(data: UserResponse) {
                val user = DataMapper.mapResponsesToEntity(data)
                localDataSource.insertUser(user)
            }

        }.asFlow()

    override fun getRepository(username: String): Flow<Resource<List<Repository>>> =
        object : NetworkBoundResource<List<Repository>, List<RepositoryResponse>>() {
            override fun loadFromDB(): Flow<List<Repository>> {
                return localDataSource.getRepository(username).map { repositoryEntity ->
                    DataMapper.mapEntitiesToDomainRepo(repositoryEntity, username)
                }
            }

            override fun shouldFetch(data: List<Repository>?): Boolean = true

            override suspend fun createCall(): Flow<ApiResponse<List<RepositoryResponse>>> =
                remoteDataSource.getRepository(username)

            override suspend fun saveCallResult(data: List<RepositoryResponse>) {
                val repositoryList = DataMapper.mapResponsesToEntitiesRepo(data, username)
                localDataSource.insertRepository(repositoryList)
            }

        }.asFlow()

    override fun getFoll(username: String, type: String): Flow<Resource<List<User>>> = flow {
        val response = remoteDataSource.getFoll(username, type)
        when (val apiResponse = response.first()) {
            is ApiResponse.Success -> {
                val userList = DataMapper.mapResponsesToEntities(apiResponse.data)
                localDataSource.insertUser(userList)
                emit(Resource.Success(DataMapper.mapEntitiesToDomain(userList)))
            }
            is ApiResponse.Empty -> {
                emit(Resource.Empty())
            }
            is ApiResponse.Error -> {
                emit(Resource.Error(apiResponse.errorMessage))
            }
        }
    }

    override fun isFavorite(username: String): Flow<Boolean> {
        return localDataSource.isFavorite(username)
    }

    override suspend fun setFavorite(username: String, state: Boolean) {
        localDataSource.updateFavorite(username, state)
    }

    override fun getFavorites(): Flow<List<User>> = flow {
        val favorites = DataMapper.mapEntitiesToDomain(localDataSource.getFavorites())
        emit(favorites)
    }.flowOn(Dispatchers.IO)
}