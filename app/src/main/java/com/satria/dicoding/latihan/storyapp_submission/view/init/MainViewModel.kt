package com.satria.dicoding.latihan.storyapp_submission.view.init

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.satria.dicoding.latihan.storyapp_submission.data.prefs.SessionPreferences
import kotlinx.coroutines.launch

class MainViewModel(private val pref: SessionPreferences) : ViewModel() {
    fun saveToken(token: String) {
        viewModelScope.launch {
            pref.saveToken(token)
        }
    }

    fun getToken(): LiveData<String?> {
        return pref.getToken().asLiveData()
    }

    fun deleteToken() {
        viewModelScope.launch {
            pref.deleteToken()
        }
    }
}