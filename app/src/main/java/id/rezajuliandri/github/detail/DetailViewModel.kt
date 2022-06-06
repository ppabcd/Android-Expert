package id.rezajuliandri.github.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.rezajuliandri.core.data.Resource
import id.rezajuliandri.core.domain.model.User
import id.rezajuliandri.core.domain.usecase.GithubUseCase
import kotlinx.coroutines.launch

class DetailViewModel(private val useCase: GithubUseCase) : ViewModel() {
    private var _detailResponse: MutableLiveData<Resource<User?>> = MutableLiveData()
    val detailResponse: LiveData<Resource<User?>> = _detailResponse

    private var _isFavoriteResponse: MutableLiveData<Boolean> = MutableLiveData()
    val isFavoriteResponse: LiveData<Boolean> = _isFavoriteResponse

    fun getDetailUser(username: String) = viewModelScope.launch {
        useCase.getDetailUser(username).collect { values ->
            _detailResponse.postValue(values)
        }
    }

    fun setFavorite(username: String) = viewModelScope.launch {
        useCase.setFavorite(username, true)
    }

    fun deleteFavorite(username: String) = viewModelScope.launch {
        useCase.setFavorite(username, false)
    }

    fun isFavorite(username: String) = viewModelScope.launch {
        useCase.isFavorite(username).collect {
            _isFavoriteResponse.postValue(it)
        }
    }
}