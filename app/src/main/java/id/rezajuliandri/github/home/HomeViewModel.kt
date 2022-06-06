package id.rezajuliandri.github.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.rezajuliandri.core.data.Resource
import id.rezajuliandri.core.domain.model.User
import id.rezajuliandri.core.domain.usecase.GithubUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.launch

@ObsoleteCoroutinesApi
@FlowPreview
@ExperimentalCoroutinesApi
class HomeViewModel(private val useCase: GithubUseCase) : ViewModel() {
    private var _searchResponse: MutableLiveData<Resource<List<User>>> = MutableLiveData()
    val searchResponse get() = _searchResponse
    fun searchUser(name: String) = viewModelScope.launch {
        useCase.searchUser(name).collect {
            searchResponse.postValue(it)
        }
    }

}