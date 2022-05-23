package id.afif.binarchallenge6.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import id.afif.binarchallenge6.Helper.DataStoreManager
import kotlinx.coroutines.launch

class UserViewModel(private val dataStoreManager: DataStoreManager) : ViewModel() {

    fun saveData(username: String, password: String) {
        viewModelScope.launch {
            dataStoreManager.setData(username, password)
        }
    }

    fun saveId(userId: Int) {
        viewModelScope.launch {
            dataStoreManager.setId(userId)
        }
    }

    fun getLoginId(): LiveData<Int> {
        return dataStoreManager.getIdLogin().asLiveData()
    }

    fun logoutSession() {
        viewModelScope.launch {
            dataStoreManager.logoutSession()
        }
    }

    fun getUserLogin(): LiveData<Array<String>> {
        return dataStoreManager.getUserLogin().asLiveData()
    }

    fun isLoggin() : LiveData<Boolean>{
        return dataStoreManager.isLoggin().asLiveData()
    }

}