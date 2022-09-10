package economical.economical.m.user_access

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import economical.economical.m.MainActivity
import economical.economical.m.R


class login : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var login:Button
    private lateinit var create_account:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        intialize_firebase()
        intialize_xmltool()
        check_userdata()
        create_account()
    }
   private fun intialize_firebase() {
        auth=FirebaseAuth.getInstance()
    }
    private fun intialize_xmltool()
    {
        email=findViewById<EditText>(R.id.login_email_data)
        password=findViewById<EditText>(R.id.login_password_data)
        login=findViewById<Button>(R.id.login_btn)
        create_account=findViewById<TextView>(R.id.login_go_create_account)
    }
    private fun check_userdata()
    {
        login.setOnClickListener()
        {
            if (TextUtils.isEmpty(email.text.toString()))
            {
                email.setError("please enter your email ")
            }
            else if (TextUtils.isEmpty(password.text.toString()))
            {
                password.setError("please enter your password ")
            }
            else{
                login_app()
            }
        }
    }

    private fun login_app() {
        auth.signInWithEmailAndPassword(email.text.toString(),password.text.toString()).addOnCompleteListener(this)
        {
            task->
            if (task.isSuccessful) {
                Toast.makeText(this, "login successfully", Toast.LENGTH_LONG).show()
                startActivity(Intent(this, MainActivity::class.java))
            }
            else
                Toast.makeText(this,"login failed",Toast.LENGTH_LONG).show()

        }
    }
    private fun create_account()
    {
        create_account.setOnClickListener{
            startActivity(Intent(this,registration::class.java))
        }
    }

}