package id.rezajuliandri.github.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class SettingViewModel(private val settingPreferences: SettingPreferences) : ViewModel() {
    private var _themeResponse: MutableLiveData<Boolean> = MutableLiveData()
    val themeResponse: LiveData<Boolean> = _themeResponse

    fun getThemeSettings() = viewModelScope.launch {
        settingPreferences.getThemeSetting().collect { values ->
            _themeResponse.postValue(values)
        }
    }
    fun saveThemeSetting(isDarkModeActive: Boolean) = viewModelScope.launch {
        settingPreferences.saveThemeSetting(isDarkModeActive)
    }
}