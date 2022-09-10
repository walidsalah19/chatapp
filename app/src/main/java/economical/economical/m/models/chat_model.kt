package economical.economical.m.models

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import economical.economical.m.data_class.message_data
import economical.economical.m.interfaces.mvvm_interface
import economical.economical.m.interfaces.user_image

class chat_model {

    private var auth:FirebaseAuth=FirebaseAuth.getInstance()
    private var senderid=auth.currentUser?.uid.toString()
    private var database:DatabaseReference=FirebaseDatabase.getInstance().getReference()
    companion object{
        private var chat_model:chat_model= chat_model()
        private var messages_arrylist:ArrayList<message_data> =ArrayList<message_data>()
        private lateinit var notify: mvvm_interface
        fun intialize_model(fragment:Fragment):chat_model
        {
            notify=fragment as mvvm_interface
            return chat_model
        }
    }
    private fun get_message(reciverid:String)
    {

        var listner=object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists())
                {
                    messages_arrylist.clear()
                    for (snap:DataSnapshot in snapshot.children)
                    {
                        val message=snap.child("message").getValue().toString()
                        val time=snap.child("time").getValue().toString()
                        val image=snap.child("image").getValue().toString()
                        val id=snap.child("id").getValue().toString()
                        messages_arrylist.add(message_data(message,time,image,id))
                    }
                    notify.notify_data()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }
        database.child("chat").child(senderid).child(reciverid).addValueEventListener(listner)
    }
    fun return_message_livedata(reciverid: String):MutableLiveData<ArrayList<message_data>>
    {
        get_message(reciverid)
        var mutablelivedata= MutableLiveData<ArrayList<message_data>>()
        mutablelivedata.postValue(messages_arrylist)
        return mutablelivedata
    }
     fun send_message(reciverid: String,message: message_data)
    {
        database.child("chat").child(senderid).child(reciverid).push().setValue(message).addOnCompleteListener {
            task->
            if (task.isSuccessful)
            {
                database.child("chat").child(reciverid).child(senderid).push().setValue(message)
            }
        }
    }
}