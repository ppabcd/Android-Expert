package id.rezajuliandri.github.followingfollowers

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import id.rezajuliandri.core.data.Resource
import id.rezajuliandri.core.domain.model.User
import id.rezajuliandri.core.domain.usecase.GithubUseCase
import id.rezajuliandri.github.Dummy
import id.rezajuliandri.github.MainDispatcherRule
import id.rezajuliandri.github.followingfollowers.FollowingFollowersFragment.FollType.FOLLOWERS
import id.rezajuliandri.github.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.junit.Assert
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@ExperimentalCoroutinesApi
@ObsoleteCoroutinesApi
@FlowPreview
@RunWith(MockitoJUnitRunner::class)
class FollowingFollowersViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var useCase: GithubUseCase
    private lateinit var viewModel: FollowingFollowersViewModel

    private val dummy = Dummy

    @Before
    fun setUp() {
        viewModel = FollowingFollowersViewModel(useCase)
    }

    @Test
    fun getFollowingFollowers() {
        val expectedResult = dummy.listUser()
        val coveredExpected: Flow<Resource<List<User>>> = flow{
            emit(Resource.Success(expectedResult))
        }
        `when`(useCase.getFoll(dummy.userData().username, "followers")).thenReturn(coveredExpected)
        viewModel.getFollowingFollowers(dummy.userData().username, FOLLOWERS)
        val actualResult = viewModel.follResponse.getOrAwaitValue()
        assertNotNull(actualResult)
        assert(actualResult is Resource.Success)
        (actualResult as Resource.Success).data?.let {
            assert(it.size == expectedResult.size)
            assertEquals(it[0].username, expectedResult[0].username)
        }
    }
}