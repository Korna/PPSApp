package com.coma.go.Web.Interface;

import android.arch.lifecycle.LiveData;

import com.coma.go.Entity.Dialog;
import com.coma.go.Entity.Event;
import com.coma.go.Entity.Profile;
import com.coma.go.Model.ApiResponse;

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

public interface IUserApi {//если в запросе NUll, то вся query с названием параметра пропадает


    @FormUrlEncoded
    @POST("/api/signup")
    Observable<Response<Void>> signup(@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("/api/signin")
    Observable<Response<Void>> login( @Field("email") String email, @Field("password") String password);


    @FormUrlEncoded
    @POST("/api/refreshFcmToken")
    Observable<Response<Void>> refreshFcmToken(  @Field("token") String token);


    @GET("/api/profile/")
    Observable<Response<Profile>> getProfile();


    @POST("/api/profile/")
    Observable<Response<Void>> updateProfile(@Body Profile profile);


    @FormUrlEncoded
    @POST("/api/notes/")
    Observable<Response<Void>> createNote(@Field("name")String name,
                                          @Field("description")String description,
                                          @Field("category")String category,
                                          @Field("image") String image,
                                          @Field("latitude") Double lat, @Field("longitude") Double lon);

    @GET("/api/notes/all")
    Observable<Response<List<Event>>> getNotes();
    @GET("/api/notes/my")
    Observable<Response<List<Event>>> getMyEvents();
    @GET("/api/notes/created")
    Observable<Response<List<Event>>> getCreatedEvents();

    @FormUrlEncoded
    @POST("/api/notes/join")
    Observable<Response<Void>> joinEvent(@Field("eventId") String eventId);

    @GET("/api/dialogs")
    Observable<Response<List<Dialog>>> getDialogs();

    @FormUrlEncoded
    @POST("/api/dialogs/create")
    Observable<Response<String>> createDialog(@Field("companionId") String companionId);
}