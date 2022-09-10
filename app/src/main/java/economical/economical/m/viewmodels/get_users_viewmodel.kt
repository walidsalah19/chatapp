package economical.economical.m.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import economical.economical.m.MainActivity
import economical.economical.m.data_class.user_data
import economical.economical.m.models.get_user_model

class get_users_viewmodel : ViewModel() {
    private lateinit var usersdata:MutableLiveData<ArrayList<user_data>>
    private lateinit var model:get_user_model

    fun get_user_data(): MutableLiveData<ArrayList<user_data>> {
        return usersdata
    }
    fun intial_viewmodel(activity: MainActivity)
    {
        var model=get_user_model.intialize_model(activity)
        usersdata=model.return_livedat()

    }
}