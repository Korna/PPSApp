package com.coma.go.Web;

import android.arch.lifecycle.LiveData;

import com.coma.go.Entity.Dialog;
import com.coma.go.Entity.Event;
import com.coma.go.Entity.Message;
import com.coma.go.Entity.Options;
import com.coma.go.Entity.Profile;
import com.coma.go.Model.ApiResponse;
import com.coma.go.Model.CreatedTokenKey;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


/**
 * Created by Koma on 11.12.2017.
 */

public interface IUserApi {
    @FormUrlEncoded
    @POST("/api/auth/signup")
    Observable<Response<Void>> signup(@Field("email") String email, @Field("password") String password);
    @FormUrlEncoded
    @POST("/api/auth/signin")
    Observable<Response<Void>> login( @Field("email") String email, @Field("password") String password,
                                      @Field("fcmToken") String fcmToken);

    @FormUrlEncoded
    @POST("/api/auth/google/")
    Observable<Response<Void>> loginGoogle( @Field("token") String token);



    @FormUrlEncoded
    @POST("/api/data/profile/updateFcm")
    Observable<Response<Void>> refreshFcmToken(@Field("fcmToken") String fcmToken);

    @GET("/api/data/profile")
    Observable<Response<Profile>> getProfile();

    @POST("/api/data/profile")
    Observable<Response<Void>> updateProfile(@Body Profile profile);

    @POST("/api/data/options")
    Observable<Response<Void>> sendOptions(@Body Options options);

    @GET("/api/data/options")
    Observable<Response<Options>> getOptions();

    @FormUrlEncoded
    @POST("/api/event/join")
    Observable<Response<Void>> joinEvent(@Field("eventId") String eventId);
    @GET("/api/event/list")
    Observable<Response<List<Event>>> getNotes();
    @GET("/api/event/myList")
    Observable<Response<List<Event>>> getMyEvents();
    @GET("/api/event/myCreateList")
    Observable<Response<List<Event>>> getCreatedEvents();

    @FormUrlEncoded
    @POST("/api/event/create")
    Observable<Response<Void>> createNote(@Field("name")String name,
                                          @Field("description")String description,
                                          @Field("category")String category,
                                          @Field("image") String image,
                                          @Field("latitude") Double lat,
                                          @Field("longitude") Double lon);




    @GET("/api/chat/dialogs/list")
    Observable<Response<List<Dialog>>> getDialogs();

    @FormUrlEncoded
    @POST("/api/chat/dialogs/create")
    Observable<Response<String>> createDialog(@Field("companionId") String companionId);


    @POST("/api/chat/message/send")
    Observable<Response<Message>> sendMessage(@Body Message message);
    @FormUrlEncoded
    @POST("/api/chat/message/list")
    Observable<Response<List<Message>>> getMessages(@Field("dialogId") String idDialog);


    @FormUrlEncoded
    @POST("/api/auth/tfa/delete")
    Observable<Response<Void>> token2fDelete(@Field("token") String token);
    @POST("/api/auth/tfa/create")
    Observable<Response<CreatedTokenKey>> token2fCreate();
    @FormUrlEncoded
    @POST("/api/auth/tfa/active")
    Observable<Response<Void>> token2fActivate(@Field("token") String token);
    @FormUrlEncoded
    @POST("/api/auth/tfa/check")
    Observable<Response<Boolean>> token2fCheck(@Field("email") String email);
    @FormUrlEncoded
    @POST("/api/auth/tfa/signin")//TODO wtf wtf wtf
    Observable<Response<Void>> token2fSignin(@Field("email") String email, @Field("password") String password, @Field("code") String code);



}