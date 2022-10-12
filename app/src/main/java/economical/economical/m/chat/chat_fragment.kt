package economical.economical.m.chat

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import economical.economical.m.R
import economical.economical.m.adapter.chat_adapter
import economical.economical.m.data_class.message_data
import economical.economical.m.interfaces.mvvm_interface
import economical.economical.m.interfaces.user_image
import economical.economical.m.notification.*
import economical.economical.m.viewmodels.chat_viewmodel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class chat_fragment : Fragment(), mvvm_interface, user_image {
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var database:DatabaseReference=FirebaseDatabase.getInstance().getReference("users")
    private lateinit var recycler_message: RecyclerView
    private lateinit var message_text: EditText
    private lateinit var send_message: FloatingActionButton
    private lateinit var viewmodel: chat_viewmodel
    private lateinit var adapter: chat_adapter
    private  var reciverid:String?=null
    private  var image_user:String?=null
    private lateinit var apiService: APIService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       val view= inflater.inflate(R.layout.fragment_chat_fragment, container, false)
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService::class.java)

        get_user_image()
         reciverid = requireArguments().getString("reciverid")
        viewmodel= ViewModelProvider(this).get(chat_viewmodel::class.java)
        viewmodel.recived_message(reciverid!!,this)
       intialize_xmltool(view)
        send_message()
        return view
    }
    private fun intialize_xmltool(view:View)
    {
        recycler_message=view.findViewById(R.id.recyclerview_chat)
        recycler_message.layoutManager=LinearLayoutManager(context)
        message_text=view.findViewById(R.id.send_message_text)
        send_message=view.findViewById(R.id.send_message)
    }
    private fun send_message()
    {
        send_message.setOnClickListener()
        {
            if (TextUtils.isEmpty(message_text.text.toString()))
            { }
            else{
                var message=message_data(message_text.text.toString(),
                get_time(),image_user!!,auth.currentUser?.uid.toString())
                viewmodel.send_message(reciverid!!,message)
                getToken("you have new message",message_text.text.toString())
                message_text.setText(" ")
            }
        }
    }
    private fun get_time():String{
        return SimpleDateFormat("hh:mm").format(Date()).toString()
    }
    private fun get_user_image()
    {
        val listner=object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                image_user =snapshot.child("image").getValue().toString()
                Log.d("image",image_user!!)

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }
        database.child(auth.currentUser?.uid.toString()).addValueEventListener(listner)

    }
    override fun notify_data() {
        viewmodel.return_recived_message().observe(this)
        {
            adapter= chat_adapter(it,auth.currentUser?.uid.toString(),this)
            recycler_message.adapter=adapter
            recycler_message.smoothScrollToPosition(it.size-1)
            adapter.notifyDataSetChanged()
        }
    }

    override fun get_image() {
    }
    private fun getToken(title: String, message: String) {
        val lisner=object:ValueEventListener
        {
            override fun onDataChange(snapshot: DataSnapshot) {
                val token =snapshot.child("token").getValue().toString()
                sendNotifications(token, title, message)

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }
        FirebaseDatabase.getInstance().getReference("Users").child(reciverid!!).addValueEventListener(lisner)
    }

    private fun sendNotifications(usertoken: String, title: String, message: String) {
        val data = Data(title, message)
        val sender = NotificationSender(data, usertoken)
        apiService.sendNotifcation(sender).enqueue(object : Callback<MyResponse?> {
            @SuppressLint("ShowToast")
            override fun onResponse(call: Call<MyResponse?>, response: Response<MyResponse?>) {
                if (response.code() == 200) {
                    if (response.body() != null && response.body()!!.success !== 1) {
                        //  Toast.makeText(getActivity(), "Failed ", Toast.LENGTH_LONG).show();
                        Log.d("tag", "error   " + response.message())
                    } else {
                        Log.d("tag",
                            response.code()
                                .toString() + " success ya Fashel " + response.body()?.success + " Token " + usertoken
                        )
                    }
                } else {
                    Log.d("tag", "Failed ya Fashel: " + response.code())
                }
            }

            override fun onFailure(call: Call<MyResponse?>, t: Throwable) {}
        })
    }

}