package economical.economical.m.adapter

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import economical.economical.m.MainActivity
import economical.economical.m.R
import economical.economical.m.chat.chat_fragment
import economical.economical.m.data_class.user_data

class user_data_adapter(
    private var arrayList: ArrayList<user_data>,
    private var activity: MainActivity
    ) : RecyclerView.Adapter<user_data_adapter.help>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): help {
       val view=LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_view_users,parent,false)
        return help(view)
    }

    override fun onBindViewHolder(holder: help, position: Int) {
            holder.name.setText(arrayList.get(position).username)
            Glide.with(activity).load(arrayList.get(position).image).into(holder.image)
        holder.itemView.setOnClickListener()
        {
            var bundle=Bundle()
            bundle.putString("reciverid",arrayList.get(position).id)
            bundle.putString("image",(arrayList.get(position).image))
            var chat=chat_fragment()
            chat.arguments=bundle
            activity.supportFragmentManager.beginTransaction().replace(R.id.main_framlayout,chat).addToBackStack(null).commit()
        }
    }
    override fun getItemCount(): Int {
       return arrayList.size
    }
    class help(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val name=itemView.findViewById<TextView>(R.id.view_user_name)
        val image=itemView.findViewById<CircleImageView>(R.id.view_user_image)
    }
}
