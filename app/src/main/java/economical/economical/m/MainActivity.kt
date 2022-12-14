package economical.economical.m

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.messaging.FirebaseMessaging
import economical.economical.m.adapter.user_data_adapter
import economical.economical.m.interfaces.mvvm_interface
import economical.economical.m.user_access.login
import economical.economical.m.viewmodels.get_users_viewmodel
import java.util.*

class MainActivity : AppCompatActivity() , mvvm_interface {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val userid=auth.currentUser?.uid.toString()
   private lateinit var toolbar:Toolbar
   private lateinit var recyclerview_users:RecyclerView
   private lateinit var viewmodel:get_users_viewmodel
   private lateinit var adapter: user_data_adapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewmodel=ViewModelProvider(this).get(get_users_viewmodel::class.java)
        viewmodel.intial_viewmodel(this)
        toolbar()
        recyclerview()
        updateToken()
    }
    private fun toolbar()
    {
        toolbar=findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
    }
    private fun recyclerview()
    {
        recyclerview_users=findViewById<RecyclerView>(R.id.recyclerview_show_users)
        recyclerview_users.layoutManager=LinearLayoutManager(this)

    }
    override fun onStart() {
        super.onStart()
        val currentuser = auth.currentUser
        if(currentuser ==null)
        {
            var intent=Intent(this, login::class.java)
            startActivity(intent)
            this.finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout)
        {
            auth.signOut()
            startActivity(Intent(this,login::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    override fun notify_data() {
        viewmodel.get_user_data()?.observe(this){
            Log.d("this2", it.size.toString())
            adapter=user_data_adapter(it,this)
            recyclerview_users.adapter=adapter
            adapter.notifyDataSetChanged()
        }
    }
    private fun updateToken() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task: Task<String> ->
                if (!task.isSuccessful) {
                    Log.d("tag", "Fetching FCM registration token failed", task.exception)
                    return@addOnCompleteListener
                }
                // Get new FCM registration toke
                Log.d("tag", task.result)
                check_token(task.result)
            }
    }
    private fun check_token(token:String) {
        FirebaseDatabase.getInstance().getReference("Users").child(userid)
            .child("token").setValue(token)

    }

}