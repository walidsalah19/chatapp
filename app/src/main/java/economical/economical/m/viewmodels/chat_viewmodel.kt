package economical.economical.m.viewmodels

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import economical.economical.m.chat.chat_fragment
import economical.economical.m.data_class.message_data
import economical.economical.m.models.chat_model

class chat_viewmodel: ViewModel() {
    private lateinit var fragment:Fragment
    private lateinit var mutablelivedata:MutableLiveData<ArrayList<message_data>>
    private lateinit var model_chat:chat_model
    fun return_recived_message():LiveData<ArrayList<message_data>>
    {
        return mutablelivedata
    }
    fun recived_message(reciverid:String,fragment:Fragment)
    {
        this.fragment=fragment
        model_chat=chat_model.intialize_model(fragment)
        mutablelivedata= model_chat.return_message_livedata(reciverid)
    }
    fun send_message(reciverid:String,message:message_data)
    {
        model_chat.send_message(reciverid, message)
    }
}