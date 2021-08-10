package study_com.study_exmp.studytogetherproject.notificationPack;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAA2OqpSEg:APA91bEDnFfFa4_tAywAUgM3IaNDorH8WOTXPl4tsqPxi988j-l7cRBiEefAi-WUnaKg8-IvC3iYn40-HG4WCwEkF5QgoSvKvXZdutBmHqK1nj-bWSlrijpjy3TbF81cotC4aRYdZdxk" // Your server key refer to video for finding your server key
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotifcation(@Body NotificationSender body);
}
