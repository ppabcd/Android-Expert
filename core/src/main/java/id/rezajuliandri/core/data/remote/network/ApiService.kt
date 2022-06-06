package id.rezajuliandri.core.data.remote.network

import id.rezajuliandri.core.data.remote.responses.ListUserResponse
import id.rezajuliandri.core.data.remote.responses.RepositoryResponse
import id.rezajuliandri.core.data.remote.responses.UserResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    suspend fun search(
        @Query("q") name: String
    ): ListUserResponse

    @GET("users/{username}")
    suspend fun getDetail(
        @Path("username") username: String
    ): UserResponse

    @GET("users/{username}/repos")
    suspend fun getRepository(
        @Path("username") username: String
    ): List<RepositoryResponse>

    @GET("users/{username}/{type}")
    suspend fun getFoll(
        @Path("username") username: String,
        @Path("type") type: String
    ): List<UserResponse>
}