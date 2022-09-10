package economical.economical.m.models

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import economical.economical.m.MainActivity
import economical.economical.m.data_class.user_data
import economical.economical.m.interfaces.mvvm_interface

class get_user_model {
    var arr_userdata= ArrayList<user_data>()

    var database: DatabaseReference = FirebaseDatabase.getInstance().getReference("users")
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    companion object {
        private lateinit var notify: mvvm_interface
        private  var get_user_model: get_user_model= get_user_model()
        fun intialize_model(activity: MainActivity): get_user_model {
            notify = activity as mvvm_interface
            return get_user_model

        }
    }

   private fun getdata() {
        val lisner= object : ValueEventListener
         {
             override fun onDataChange(snapshot: DataSnapshot) {
                 if (snapshot.exists()) {
                     arr_userdata.clear()
                     for (snap: DataSnapshot in snapshot.children) {
                         val id = snap.child("id").getValue().toString()
                         if (!id.equals(auth.currentUser?.uid)) {
                             val name = snap.child("username").getValue().toString()
                             val image = snap.child("image").getValue().toString()
                             val email = snap.child("email").getValue().toString()

                             arr_userdata.add(user_data(name, email, image, id))
                             Log.e("this", name)
                         }
                     }
                     notify.notify_data()
                 }
             }

             override fun onCancelled(error: DatabaseError) {
                 TODO("Not yet implemented")
             }

         }
        database.addValueEventListener(lisner)
    }
    fun return_livedat():MutableLiveData<ArrayList<user_data>>
    {
        getdata()
        var mut=MutableLiveData<ArrayList<user_data>>()
        mut.postValue(arr_userdata)
        return mut
    }

}
