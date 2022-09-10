package economical.economical.m.user_access

import android.R.attr
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.widget.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import de.hdodenhof.circleimageview.CircleImageView
import economical.economical.m.MainActivity
import economical.economical.m.R
import economical.economical.m.data_class.user_data
import java.io.InputStream
import java.net.URI
import java.util.*


class registration : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database:DatabaseReference
    private lateinit var username: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var profile_image: CircleImageView
    private lateinit var create_account_btn: Button
    private lateinit var select_image: TextView
    private lateinit var imageUri:Uri
    private var image="image"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        intialize_firebase()
        intialize_xmltool()
        select_image()
        chech_user_data()
    }
    fun intialize_firebase()
    {
        auth=FirebaseAuth.getInstance()
        database=FirebaseDatabase.getInstance().getReference("users")
    }
    fun intialize_xmltool()
    {
        username=findViewById<EditText>(R.id.registration_username_data)
        password=findViewById<EditText>(R.id.registration_password_data)
        profile_image=findViewById<CircleImageView>(R.id.add_profile_image)
        email=findViewById<EditText>(R.id.registration_email_data)
        select_image=findViewById<EditText>(R.id.select_profile_image)
        create_account_btn=findViewById<Button>(R.id.create_account_btn)
    }
    private  fun select_image()
    {
        select_image.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 1)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 1&&data != null) {
                imageUri = data.data!!
            upload_image_database()
            Glide.with(this).load(data.data).into(profile_image)
        }
    }

    private fun upload_image_database() {
        var dialog:ProgressDialog= ProgressDialog(this)
        dialog.setTitle("upload image")
        dialog.setCancelable(false)
        dialog.show()
        val fileName = UUID.randomUUID().toString() +".jpg"
        val refStorage = FirebaseStorage.getInstance().reference.child("images/$fileName")

        refStorage.putFile(imageUri)
            .addOnSuccessListener(
                OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot ->
                    taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                        image = it.toString()
                        dialog.dismiss()
                    }
                })

            ?.addOnFailureListener(OnFailureListener { e ->
                print(e.message)
            })
    }

    private fun chech_user_data()
   {
       create_account_btn.setOnClickListener {
           if (TextUtils.isEmpty(username.text.toString()))
           {
               username.setError("please enter your name")
           }
          else if (TextUtils.isEmpty(email.text.toString()))
           {
               email.setError("please enter your email")
           }
          else if (TextUtils.isEmpty(password.text.toString()))
           {
               password.setError("please enter your password")
           }
           else{
               create_account()
           }

       }
   }

    private fun create_account() {
        auth.createUserWithEmailAndPassword(email.text.toString(),password.text.toString()).addOnCompleteListener{
            task->
            if (task.isSuccessful)
            {
                add_account_to_database(task.getResult().user?.uid.toString())
            }
        }
    }

    private fun add_account_to_database(id: String) {
        var user:user_data=user_data(username.text.toString(),email.text.toString(),image,id)
          database.child(id).setValue(user).addOnCompleteListener {
              task->
              if (task.isSuccessful)
              {  Toast.makeText(this,"create account successfully",Toast.LENGTH_LONG).show()
                 startActivity(Intent(this, MainActivity::class.java))
              }
              else
                  Toast.makeText(this,"create account failed",Toast.LENGTH_LONG).show()
          }
    }
}