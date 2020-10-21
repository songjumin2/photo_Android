package com.songjumin.photoapp2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.songjumin.photoapp2.adapter.RecyclerViewAdapter;
import com.songjumin.photoapp2.api.NetworkClient;
import com.songjumin.photoapp2.api.PostApi;
import com.songjumin.photoapp2.api.UserApi;
import com.songjumin.photoapp2.model.Item;
import com.songjumin.photoapp2.model.Post;
import com.songjumin.photoapp2.model.PostRes;
import com.songjumin.photoapp2.model.UserRes;
import com.songjumin.photoapp2.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Welcome extends AppCompatActivity {

    Button btn_logout;

    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;
    List<Item> postArrayList = new ArrayList<>();

    String token;

    Button btnPosting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        btn_logout = findViewById(R.id.btn_logout);
        recyclerView = findViewById(R.id.recyclerView);
        btnPosting = findViewById(R.id.btnPosting);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(Welcome.this));

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 1. 쉐어드 프리퍼런스에 저장되어있는 토큰을 가져온다.
                SharedPreferences sharedPreferences =
                        getSharedPreferences(Utils.PREFERENCES_NAME, MODE_PRIVATE);
                final String token = sharedPreferences.getString("token", null);
                Log.i("AAA", token);

                Retrofit retrofit = NetworkClient.getRetrofitClient(Welcome.this);
                UserApi userApi = retrofit.create(UserApi.class);

                Call<UserRes> call = userApi.logoutUser("Bearer "+token);
                call.enqueue(new Callback<UserRes>() {
                    @Override
                    public void onResponse(Call<UserRes> call, Response<UserRes> response) {
                        if(response.isSuccessful()){
                            if(response.body().isSuccess()){
                                SharedPreferences sp = getSharedPreferences(Utils.PREFERENCES_NAME,
                                        MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("token", null);
                                editor.apply();

                                Intent i = new Intent(Welcome.this, Login.class);
                                startActivity(i);
                                finish();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserRes> call, Throwable t) {

                    }
                });
            }
        });


        SharedPreferences sp = getSharedPreferences(Utils.PREFERENCES_NAME, MODE_PRIVATE);
        token = sp.getString("token", null);

        Log.i("AAA", "token : "+token);

        getNetworkData();

        btnPosting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Welcome.this, Posting.class);
                startActivity(i);
            }
        });

    }

    private void getNetworkData() {
        Retrofit retrofit = NetworkClient.getRetrofitClient(Welcome.this);

        PostApi postsApi = retrofit.create(PostApi.class);

        Call<PostRes> call = postsApi.getPosts("Bearer "+token, 0, 25);

        call.enqueue(new Callback<PostRes>() {
            @Override
            public void onResponse(Call<PostRes> call, Response<PostRes> response) {
                // response.body() => PostRes 클래스
                Log.i("AAA", response.body().getSuccess().toString());
                // response.body().get(0) => List<Item> 의 첫번째 Item 객체.
                // response.body().get(0).getContent() => 위의 Item 객체에 저장된 content 값
                Log.i("AAA", response.body().getItems().get(0).getContent());
                Log.i("AAA", response.body().getCnt().toString());
                postArrayList = response.body().getItems();
                adapter = new RecyclerViewAdapter(Welcome.this, postArrayList);
                recyclerView.setAdapter(adapter);
            }
            @Override
            public void onFailure(Call<PostRes> call, Throwable t) {
            }
        });
    }


}