package com.chunhuitech.reader.service;

import com.chunhuitech.reader.entity.BaseResult;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
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

}
