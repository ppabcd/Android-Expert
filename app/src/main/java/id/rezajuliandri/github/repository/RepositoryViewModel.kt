package id.rezajuliandri.github.repository

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.rezajuliandri.core.data.Resource
import id.rezajuliandri.core.domain.model.Repository
import id.rezajuliandri.core.domain.usecase.GithubUseCase
import kotlinx.coroutines.launch

class RepositoryViewModel(private val useCase: GithubUseCase) : ViewModel() {
    private var _repositoryResponse: MutableLiveData<Resource<List<Repository>>> = MutableLiveData()
    val repositoryResponse get() = _repositoryResponse

    fun getRepository(username: String) = viewModelScope.launch {
        useCase.getRepository(username).collect {
            _repositoryResponse.postValue(it)
        }
    }
}