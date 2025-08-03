package cz.mendelu.pef.cookit.activity

import androidx.lifecycle.ViewModel
import cz.mendelu.pef.cookit.database.datastore.IDataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AppIntroViewModel @Inject constructor(private val dataStoreRepository: IDataStoreRepository) : ViewModel() {

    suspend fun setFirstRun(){
        dataStoreRepository.setFirstRun()
    }
}