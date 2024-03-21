package com.example.advweek4.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.advweek4.model.Student
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ListViewModel(application: Application): AndroidViewModel(application) {
    val studentsLD = MutableLiveData<ArrayList<Student>>()
    val studentLoadErrorLD = MutableLiveData<Boolean>()
    val loadingLD = MutableLiveData<Boolean>()

    val TAG = "volleyTag"
    private var queue: RequestQueue? = null

    init {
        queue = Volley.newRequestQueue(getApplication())
    }

    fun refresh() {
        loadingLD.value = true
        studentLoadErrorLD.value = false

        queue?.let { queue ->
            val url = "http://adv.jitusolution.com/student.php"

            val stringRequest = StringRequest(
                Request.Method.GET, url,
                { response ->
                    val sType = object : TypeToken<List<Student>>() { }.type
                    val result = Gson().fromJson<List<Student>>(response, sType)
                    studentsLD.value = result as ArrayList<Student>?
                    loadingLD.value = false
                    Log.d("showvoley", "Response: $response") // Log the response from the server
                    Log.d("showvoley", "Parsed Result: $result") // Log the parsed result
                },
                { error ->
                    Log.d("showvoley", "Error: $error") // Log any error that occurred
                    studentLoadErrorLD.value = true
                    loadingLD.value = false
                })

            stringRequest.tag = TAG
            queue?.add(stringRequest)
        }
    }

    override fun onCleared() {
        super.onCleared()
        queue?.cancelAll(TAG)
    }
}

