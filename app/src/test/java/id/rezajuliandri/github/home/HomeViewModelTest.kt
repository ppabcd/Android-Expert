package id.rezajuliandri.github.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import id.rezajuliandri.core.data.Resource
import id.rezajuliandri.core.domain.model.User
import id.rezajuliandri.core.domain.usecase.GithubUseCase
import id.rezajuliandri.github.Dummy
import id.rezajuliandri.github.MainDispatcherRule
import id.rezajuliandri.github.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import kotlin.test.assertNotNull

@ExperimentalCoroutinesApi
@ObsoleteCoroutinesApi
@FlowPreview
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var useCase: GithubUseCase
    private lateinit var viewModel: HomeViewModel

    private val dummy = Dummy

    @Before
    fun setUp() {
        viewModel = HomeViewModel(useCase)
    }

    @Test
    fun searchUser() = runTest {
        val expectedResult = Dummy.listUser()
        val coveredExpected: Flow<Resource<List<User>>> = flow {
            emit(Resource.Success(expectedResult))
        }
        `when`(
            useCase.searchUser(
                dummy.userData().username
            )
        ).thenReturn(coveredExpected)
        viewModel.searchUser(dummy.userData().username)
        val actualResult = viewModel.searchResponse.getOrAwaitValue()
        assertNotNull(actualResult)
        assertTrue(actualResult is Resource.Success)
        (actualResult as Resource.Success).data?.let {
            assertTrue(it.size == expectedResult.size)
            assertTrue(it[0].username == expectedResult[0].username)
        }
    }
}