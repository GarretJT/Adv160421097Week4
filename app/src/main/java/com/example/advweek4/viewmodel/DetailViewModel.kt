package com.example.advweek4.viewmodel


import android.app.Application
import android.content.ContentValues.TAG
import android.database.Observable
import android.util.Log
import android.widget.Button
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.advweek4.R
import com.example.advweek4.model.Student
import com.example.advweek4.view.MainActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.concurrent.TimeUnit


class DetailViewModel(application: Application): AndroidViewModel(application) {
    val loadingLD = MutableLiveData<Boolean>()
    val studentLoadErrorLD = MutableLiveData<Boolean>()
    val studentLD = MutableLiveData<Student>()
    private var queue: RequestQueue? = null


    init {
        queue = Volley.newRequestQueue(getApplication())
    }

    fun fetch(id: String) {

        queue?.let { queue ->
            val url = "http://adv.jitusolution.com/student.php?id=${id}"

            val stringRequest = StringRequest(
                Request.Method.GET, url,
                { response ->
                    val sType = object : TypeToken<Student>() {}.type
                    val result = Gson().fromJson<Student>(response, sType)
                    studentLD.value = result as Student?
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

    fun fetcha() {
        val student1 = Student("16055","Nonie","1998/03/28","5718444778",
            "http://dummyimage.com/75x100.jpg/cc0000/ffffff")
        studentLD.value = student1
    }
}