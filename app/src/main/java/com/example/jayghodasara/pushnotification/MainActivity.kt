package com.example.jayghodasara.pushnotification

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.database.*
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    lateinit var database: FirebaseDatabase
    lateinit var databaseReference: DatabaseReference
    lateinit var ref: DatabaseReference
    lateinit var ref2: DatabaseReference
    lateinit var serverkey: String
    var tokenlist: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        database = FirebaseDatabase.getInstance()
        databaseReference = database.reference

        ref = databaseReference.child("User")
        ref.child("MI").child("Token").setValue(FirebaseInstanceId.getInstance().token)

        databaseReference.child("Server").child("Server_key").setValue("AAAApNwK_T0:APA91bGlLPgMfbCwl61IMebhKpxAAhtunX9FPUi499QxSHU9qV3ooX2j-l8zCBQOFJtmdqILtoPIgCz2ZL4EGXVRbXD9x_ermXjsAgnnzYPo2x1Bn3qY9nOJRwGUpSAtSCgv4hAwCswRygTzubatun5nXt7Dz5VtSA")

        ref2 = FirebaseDatabase.getInstance().reference.child("")

        var query: Query = ref2.child("Server").orderByValue()

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                var dataSnapshot: DataSnapshot = p0.children.iterator().next()
                serverkey = dataSnapshot.value.toString()
                Log.i("server_key", serverkey.toString())
            }

        })

        var query2: Query = ref2.child("User").orderByValue()

        query2.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {

                for (snap: DataSnapshot in p0.children) {
                    var token: String = snap.child("Token").value.toString()
                    Log.i("tokens", token)
                    tokenlist.add(token)

                }
            }

        })

        sendreq.setOnClickListener(View.OnClickListener {

            var message: String = msg.text.toString()
            var title: String = et_title.text.toString()
            var retrofit: Retrofit = Retrofit.Builder().baseUrl("https://fcm.googleapis.com/fcm/").addConverterFactory(GsonConverterFactory.create()).build()

            var retrointerface: RetrofitCallInterface = retrofit.create(RetrofitCallInterface::class.java)

            var map: HashMap<String, String> = HashMap()
            map["Content-Type"] = "application/json"
            map["Authorization"] = "key=$serverkey"

            for (token: String in tokenlist) {
                var data: Data = Data(message, title, "data-Type")

                var fcm: FCM = FCM(token, data)

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


            }
        })

    }
}
