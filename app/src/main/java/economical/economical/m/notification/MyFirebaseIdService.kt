package economical.economical.m.notification

import android.media.session.MediaSession
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.auth.Token
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService

class MyFirebaseIdService : FirebaseMessagingService() {
   lateinit var  refreshToken : String
    override fun onNewToken(s:String)
        {
            super.onNewToken(s)
            val firebaseUser = FirebaseAuth.getInstance().currentUser
            FirebaseMessaging.getInstance().token.addOnCompleteListener {
                task->
                if (!task.isSuccessful()) {
                    Log.w("TAG", "Fetching FCM registration token failed", task.getException())
                    return@addOnCompleteListener
                }
                // Get new FCM registration token
                // Get new FCM registration token
                refreshToken = task.getResult()
                println(refreshToken)
                if (firebaseUser != null) {
                    updateToken(refreshToken)
                }
            }
        }
   fun  updateToken( refreshToken:String)
   {
       val firebaseUser = FirebaseAuth.getInstance().currentUser
       val token1 = Token(refreshToken)
       FirebaseDatabase.getInstance().getReference("Users").child(
           FirebaseAuth.getInstance().currentUser!!.uid
       ).child("token").setValue(token1)
   }
}