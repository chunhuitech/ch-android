package com.chunhuitech.reader.service;

import com.chunhuitech.reader.App;
import com.chunhuitech.reader.callback.IDownloadCallback;
import com.chunhuitech.reader.callback.ILoadDataCallback;
import com.chunhuitech.reader.config.ServerConfig;
import com.chunhuitech.reader.entity.BaseConverterFactory;
import com.chunhuitech.reader.entity.BaseResult;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DataService {

    Retrofit retrofit;
    IDataService iDataService;

    public DataService() {
        this.retrofit = new Retrofit.Builder()
                .baseUrl(ServerConfig.HOST)
                .addConverterFactory(BaseConverterFactory.create())//GsonConverterFactory.create()
                .build();

        iDataService = this.retrofit.create(IDataService.class);
    }

    public Object getChildren(int id, final ILoadDataCallback iLoadDataCallback) {
        App.instanceApp().getMessageService().showWaitingDialog();
        Call<BaseResult> call = iDataService.getChildren(id);
        call.enqueue(new Callback<BaseResult>() {
            @Override
            public void onResponse(Call<BaseResult> call, Response<BaseResult> response) {
                App.instanceApp().getMessageService().hideWaitingDialog();
                iLoadDataCallback.loadFinish(response.body());
            }

            @Override
            public void onFailure(Call<BaseResult> call, Throwable t) {
                t.printStackTrace();
                App.instanceApp().getMessageService().hideWaitingDialog();
            }
        });
        return null;
    }

    public Object getBookPages(String bookId, final ILoadDataCallback iLoadDataCallback) {
        App.instanceApp().getMessageService().showWaitingDialog();
        Call<BaseResult> call = iDataService.getBookPages(bookId, 50, 1);
        call.enqueue(new Callback<BaseResult>() {
            @Override
            public void onResponse(Call<BaseResult> call, Response<BaseResult> response) {
                App.instanceApp().getMessageService().hideWaitingDialog();
                iLoadDataCallback.loadFinish(response.body());
            }

            @Override
            public void onFailure(Call<BaseResult> call, Throwable t) {
                App.instanceApp().getMessageService().hideWaitingDialog();
                iLoadDataCallback.loadFinish(null);
            }
        });
        return null;
    }

    public Object getPageReadInfo(String pageId, final ILoadDataCallback iLoadDataCallback) {
        Call<BaseResult> call = iDataService.getPageReadInfo(pageId);
        call.enqueue(new Callback<BaseResult>() {
            @Override
            public void onResponse(Call<BaseResult> call, Response<BaseResult> response) {
                iLoadDataCallback.loadFinish(response.body());
            }

            @Override
            public void onFailure(Call<BaseResult> call, Throwable t) {
                iLoadDataCallback.loadFinish(null);
            }
        });
        return null;
    }

    public void getBookResource(String pageId, final ILoadDataCallback iLoadDataCallback) {
        Call<BaseResult> call = iDataService.getBookResource(pageId);
        call.enqueue(new Callback<BaseResult>() {
            @Override
            public void onResponse(Call<BaseResult> call, Response<BaseResult> response) {
                iLoadDataCallback.loadFinish(response.body());
            }

            @Override
            public void onFailure(Call<BaseResult> call, Throwable t) {
                iLoadDataCallback.loadFinish(null);
            }
        });
    }

    public void downloadResource(String url, final IDownloadCallback iDownloadCallback) {
        Call<ResponseBody> call = iDataService.downloadResource(url);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iDownloadCallback.loadFinish(response.body());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                iDownloadCallback.loadFinish(null);
            }
        });
    }

}
