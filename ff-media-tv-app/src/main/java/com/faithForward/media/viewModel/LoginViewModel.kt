package com.faithForward.media.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faithForward.network.dto.login.ErrorResponse
import com.faithForward.repository.NetworkRepository
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject
constructor(
    private val networkRepository: NetworkRepository
) : ViewModel() {


    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = networkRepository.loginUser(email, password)

                if (response.isSuccessful) {
                    val body = response.body()
                    Log.d("Login", "Success: ${body?.message}")
                    // handle success (update UI/state)
                } else {
                    val errorBody = response.errorBody()
                    val gson = Gson()
                    val error = errorBody?.charStream()?.let {
                        gson.fromJson(it, ErrorResponse::class.java)
                    }

                    Log.e("Login", "Error: ${error?.message}")
                    error?.errors?.forEach { (field, messages) ->
                        messages.forEach { msg ->
                            Log.e("Login", "$field: $msg")
                            // You can collect these messages into a string and show in UI
                        }
                    }
                }

            } catch (e: Exception) {
                Log.e("Login", "Exception: ${e.message}")
            }
        }
    }


}