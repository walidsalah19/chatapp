package economical.economical.m.notification

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface APIService {
    @Headers(
        "Content-Type:application/json",
        "Authorization:key=AAAA4_C4Ndc:APA91bG5r58gi6y9Z73rC_fEjFTSTIauQUUSKto5GXO8onTYwCONoTsAOpSl_0v5j2pq8FMAu7DaOIBb4DXaPAAnk1JywmQujUPf68o9-Pv3wQhk-7eY4dXcdkKuBPpvQejOze4frXjo" // Your server key refer to video for finding your server key
    )
    @POST("fcm/send")
     fun sendNotifcation(@Body body: NotificationSender?): Call<MyResponse>
}