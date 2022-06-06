package id.rezajuliandri.github.followingfollowers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.rezajuliandri.core.data.Resource
import id.rezajuliandri.core.domain.model.User
import id.rezajuliandri.core.domain.usecase.GithubUseCase
import id.rezajuliandri.github.followingfollowers.FollowingFollowersFragment.FollType
import kotlinx.coroutines.launch

class FollowingFollowersViewModel(private val useCase: GithubUseCase) : ViewModel() {

    private var _follResponse: MutableLiveData<Resource<List<User>>> = MutableLiveData()
    val follResponse: LiveData<Resource<List<User>>> = _follResponse
    fun getFollowingFollowers(username: String, type: FollType) = viewModelScope.launch {
        useCase.getFoll(
            username, when (type) {
                FollType.FOLLOWING -> "following"
                FollType.FOLLOWERS -> "followers"
            }
        ).collect {
            _follResponse.postValue(it)
        }
    }
}