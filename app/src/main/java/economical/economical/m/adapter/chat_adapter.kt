package economical.economical.m.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import economical.economical.m.R
import economical.economical.m.data_class.message_data

class chat_adapter (private var messages_arrayList:ArrayList<message_data>?,
            private var senderid:String, private var fragment:Fragment) : RecyclerView.Adapter<chat_adapter.help>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): help {
       val view= LayoutInflater.from(parent.context).inflate(R.layout.chat_recycler_layout,parent,false)
        return help(view)
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onBindViewHolder(holder: help, position: Int) {
        if (messages_arrayList?.get(position)?.image.equals("image"))
        {
            if (messages_arrayList?.get(position)?.id.equals(senderid)) {
                holder.sender_layout.visibility = View.VISIBLE
                holder.reciver_layout.visibility = View.INVISIBLE
                holder.sender_message.setText(messages_arrayList?.get(position)?.message)
                holder.sender_time.setText(messages_arrayList?.get(position)?.time)
            } else {
                holder.sender_layout.visibility = View.INVISIBLE
                holder.reciver_layout.visibility = View.VISIBLE
                holder.reciver_message.setText(messages_arrayList?.get(position)?.message)
                holder.reciver_time.setText(messages_arrayList?.get(position)?.time)
            }
        }else {
            if (messages_arrayList?.get(position)?.id.equals(senderid)) {
                holder.sender_layout.visibility = View.VISIBLE
                holder.reciver_layout.visibility = View.INVISIBLE
                Glide.with(fragment.activity!!).load(messages_arrayList?.get(position)?.image)
                    .into(holder.sender_image)
                holder.sender_message.setText(messages_arrayList?.get(position)?.message)
                holder.sender_time.setText(messages_arrayList?.get(position)?.time)
            } else {
                holder.sender_layout.visibility = View.INVISIBLE
                holder.reciver_layout.visibility = View.VISIBLE
                Glide.with(fragment.activity!!).load(messages_arrayList?.get(position)?.image)
                    .into(holder.reciver_image)
                holder.reciver_message.setText(messages_arrayList?.get(position)?.message)
                holder.reciver_time.setText(messages_arrayList?.get(position)?.time)
            }
        }
    }

    override fun getItemCount(): Int {
        return messages_arrayList!!.size
    }
    class help(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
       val sender_image=itemView.findViewById<CircleImageView>(R.id.sender_image)
        val sender_message=itemView.findViewById<TextView>(R.id.sender_message)
        val sender_time=itemView.findViewById<TextView>(R.id.sender_time)
        val sender_layout=itemView.findViewById<RelativeLayout>(R.id.sender_layout)
        val reciver_layout=itemView.findViewById<RelativeLayout>(R.id.reciver_layout)
        val reciver_image=itemView.findViewById<CircleImageView>(R.id.reciver_image)
        val reciver_message=itemView.findViewById<TextView>(R.id.reciver_message)
        val reciver_time=itemView.findViewById<TextView>(R.id.reciver_time)

    }

}