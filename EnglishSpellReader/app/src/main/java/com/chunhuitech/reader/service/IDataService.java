package com.chunhuitech.reader.service;

import com.chunhuitech.reader.entity.BaseResult;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface IDataService {

    @GET("api/admin/api/class/children")
    Call<BaseResult> getChildren(@Query("id") int id);

    @GET("api/admin/api/record/get")
    Call<BaseResult> getBookPages(@Query("classId") String bookId,
                                  @Query("limit") Integer pageSize,
                                  @Query("page") Integer currentPage);

    @GET("api/admin/api/readpoint/get")
    Call<BaseResult> getPageReadInfo(@Query("pageId") String pageId);

    @GET("api/admin/api/resource/get")
    Call<BaseResult> getBookResource(@Query("id") String bookId);

    @Streaming
    @GET
    Call<ResponseBody> downloadResource(@Url String url);

    @GET("api/admin/api/resource/get")
    Call<BaseResult> getBookPages2(@Query("id") String bookId);


    @Headers({
            "Content-Type: application/json;charset=UTF-8",
            "User-Agent: Retrofit-your-App"})
    //@FormUrlEncoded
    @POST("api/admin/api/prodactivity/report")
//    Call<ResponseBody> addStartupEvent(@Field("clientFlag") String clientFlag, @Field("procName") String procName, @Field("procVersion") String procVersion,
//                                       @Field("procId") Long procId, @Field("os") String os, @Field("eventName") String eventName);
    Call<ResponseBody> addStartupEvent(@Body RequestBody productActivity);

}
