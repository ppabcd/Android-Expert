package id.rezajuliandri.github.favorites

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.rezajuliandri.core.domain.model.User
import id.rezajuliandri.core.domain.usecase.GithubUseCase
import kotlinx.coroutines.launch

class FavoriteViewModel(private val useCase: GithubUseCase) : ViewModel() {
    private var _favoritesResponse: MutableLiveData<List<User>> = MutableLiveData()
    val favoritesResponse get() = _favoritesResponse
    fun getFavorites() = viewModelScope.launch {
        useCase.getFavorites().collect {
            _favoritesResponse.postValue(it)
        }
    }
}