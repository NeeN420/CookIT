package cz.mendelu.pef.cookit.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.pef.cookit.database.datastore.IDataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenActivityViewModel @Inject constructor(
    private val dataStoreRepository: IDataStoreRepository
) : ViewModel() {

    private val _splashScreenState = MutableStateFlow(SplashScreenUiState())
    val splashScreenState: StateFlow<SplashScreenUiState> = _splashScreenState

    init {
        checkAppState()
    }

    private fun checkAppState(){
        viewModelScope.launch {
            if (dataStoreRepository.getFirstRun()){
                _splashScreenState.value = SplashScreenUiState(runForAFirstTime = true)
            } else {
                _splashScreenState.value = SplashScreenUiState(continueToApp = true)
            }

        }
    }
}