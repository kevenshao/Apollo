package com.kevens.apollo;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kevens.apollo.data.service.ApiService;
import com.kevens.basic.data.http.ApolloSubscriber;
import com.kevens.basic.data.http.HttpClient;
import com.kevens.basic.data.model.ApiException;
import com.kevens.basic.data.model.RequestBodyProgress;
import com.kevens.basic.data.model.ResponseData;

import org.json.JSONException;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.btn_request)
    Button mBtnReq;

    @Bind(R.id.txt_response)
    TextView mText;

    @Bind(R.id.progress_bar)
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mBtnReq.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_request:
                doRequest();
                break;
        }
    }

    private void doRequest() {
        mProgressBar.setVisibility(View.VISIBLE);

        ApiService apiService = HttpClient.getIns(ApiService.WEB_API_BASE).createService(ApiService.class);

        File file = new File(Environment.getExternalStorageDirectory()+"/bbb.png");
        RequestBodyProgress requestBody = new RequestBodyProgress(file, uploadLister);
        apiService.uploadFile(file.getName(), requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApolloSubscriber<ResponseData<Object>>() {
                    @Override
                    protected void onError(ApiException ex) {
                        mProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    protected void onPermissionError(ApiException ex) {

                    }

                    @Override
                    protected void onResultError(ApiException ex) {
                        mProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onNext(ResponseData<Object> responseData) {
                        Log.i("kevens", "onNext");
//                        try {
//                            Log.e("kevens", "response = " + responseData.getDataJsonObj().getString("name") +
//                                    " age=" + responseData.getDataJsonObj().getInt("age"));
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
                    }

                    @Override
                    public void onCompleted() {
                        mProgressBar.setVisibility(View.GONE);
                    }
                });


//        Call<ResponseData<Object>> call = apiService.getMethod("kevenshao");
//        call.enqueue(new Callback<ResponseData<Object>>() {
//            @Override
//            public void onResponse(Call<ResponseData<Object>> call1, Response<ResponseData<Object>> response) {
//                mProgressBar.setVisibility(View.GONE);
//                if (response.isSuccessful()) {
//                    try {
//                        Log.e("kevens", "response = " + response.body().getDataJsonObj().getString("name") +
//                                " age=" + response.body().getDataJsonObj().getInt("age"));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    // 404 - 找不到对应的接口
//                    Log.e("kevens", "failed:" + response.code());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseData<Object>> call1,Throwable t) {
//                //[无网络时]
//                //java.net.UnknownHostException: Unable to resolve host "kevens.wicp.net": No address associated with hostname
//                Log.e("kevens", "failure=" + t.toString());
//                mProgressBar.setVisibility(View.GONE);
//                mText.setText(t.getMessage());
//            }
//        });
    }

    RequestBodyProgress.UploadCallbacks uploadLister = new RequestBodyProgress.UploadCallbacks() {
        @Override
        public void onProgressUpdate(int percentage) {
            Log.i("kevens", "Upload percentage = " + percentage);
        }
    };
}
