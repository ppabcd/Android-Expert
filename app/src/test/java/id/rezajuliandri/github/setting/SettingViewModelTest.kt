package id.rezajuliandri.github.setting

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import id.rezajuliandri.github.MainDispatcherRule
import id.rezajuliandri.github.getOrAwaitValue
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
internal class SettingViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var preferences: SettingPreferences
    private lateinit var viewModel: SettingViewModel

    @Before
    fun setUp() {
        viewModel = SettingViewModel(preferences)
    }

    @Test
    fun testGetThemeSetting() {
        val expectedResult = true
        `when`(preferences.getThemeSetting()).thenReturn(flowOf(expectedResult))
        viewModel.getThemeSettings()
        val actualResult = viewModel.themeResponse.getOrAwaitValue()
        assertNotNull(actualResult)
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun testSetThemeSetting() = runTest{
        viewModel.saveThemeSetting(true)
        verify(preferences).saveThemeSetting(true)
    }
}