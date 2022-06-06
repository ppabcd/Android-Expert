package id.rezajuliandri.core.data.remote

import android.util.Log
import id.rezajuliandri.core.data.remote.network.ApiResponse
import id.rezajuliandri.core.data.remote.network.ApiService
import id.rezajuliandri.core.data.remote.responses.RepositoryResponse
import id.rezajuliandri.core.data.remote.responses.UserResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RemoteDataSource(private val apiService: ApiService) {
    fun search(username: String): Flow<ApiResponse<List<UserResponse>>> = flow {
        try {
            val response = apiService.search(username)
            val dataArray = response.items
            if (dataArray.isNotEmpty()) {
                emit(ApiResponse.Success(dataArray))
            } else {
                emit(ApiResponse.Empty)
            }
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.toString()))
        }
    }.flowOn(Dispatchers.IO)

    fun getDetail(username: String): Flow<ApiResponse<UserResponse>> = flow {
        try {
            val response = ApiResponse.Success(apiService.getDetail(username))
            emit(response)
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.toString()))
        }
    }.flowOn(Dispatchers.IO)

    fun getRepository(username: String): Flow<ApiResponse<List<RepositoryResponse>>> = flow {
        try {
            val response = apiService.getRepository(username)
            if (response.isNotEmpty()) {
                emit(ApiResponse.Success(response))
            } else {
                emit(ApiResponse.Empty)
            }
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.toString()))
        }
    }.flowOn(Dispatchers.IO)

    fun getFoll(username: String, type: String): Flow<ApiResponse<List<UserResponse>>> = flow {
        try {
            val response = apiService.getFoll(username, type)
            if (response.isNotEmpty()) {
                emit(ApiResponse.Success(response))
            } else {
                emit(ApiResponse.Empty)
            }
        } catch (e: Exception){
            emit(ApiResponse.Error(e.toString()))
        }
    }.flowOn(Dispatchers.IO)
}