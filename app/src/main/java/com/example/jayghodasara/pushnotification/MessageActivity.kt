package com.example.jayghodasara.pushnotification

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_message.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MessageActivity : AppCompatActivity() {


    lateinit var database: FirebaseDatabase
    lateinit var dataref: DatabaseReference
    var token: String? = null
    var serverkey: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)

        var users: ArrayList<String> = intent.getSerializableExtra("users") as ArrayList<String>
        var hash: HashMap<String, String> = intent.getSerializableExtra("hashmap") as HashMap<String, String>
        serverkey = intent.extras["server_key"].toString()
        Log.i("server_key2", serverkey.toString())

        database = FirebaseDatabase.getInstance()
        dataref = database.reference.child("")


        var arrayadapter: ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_spinner_item, users)
        arrayadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner1.adapter = arrayadapter

        send.setOnClickListener(View.OnClickListener {

            var message: String = text_msg.text.toString()

            var selected_value: String = spinner1.selectedItem.toString()

            token = hash[selected_value]


            var map: HashMap<String, String> = HashMap()
            map["Content-Type"] = "application/json"
            map["Authorization"] = "key=$serverkey"
            var retrofit: Retrofit = Retrofit.Builder().baseUrl("https://fcm.googleapis.com/fcm/").addConverterFactory(GsonConverterFactory.create()).build()
            var retrointerface: RetrofitCallInterface = retrofit.create(RetrofitCallInterface::class.java)
            var data: Data = Data(message, "FCM", "Personal_Message")
            var fcm: FCM = FCM(token!!, data)
            var call: Call<ResponseBody> = retrointerface.sendreq(map, fcm)
            call.enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                    Toast.makeText(applicationContext, "Error", Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                    Toast.makeText(applicationContext, "Push message Triggered", Toast.LENGTH_LONG).show()
                    Log.i("Response", response.toString())
                }
            })

        })

    }
}
