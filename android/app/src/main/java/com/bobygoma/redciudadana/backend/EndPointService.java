package com.bobygoma.redciudadana.backend;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.bobygoma.redciudadana.utils.Commons;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by daniel.streitenberger on 09/03/2017.
 */
public class EndPointService {
    //public static final String BASE_URL = "http://redciudadana.hopto.org/";
    public static final String BASE_URL_DEV = "http://104.154.60.179:8080/";
    public static final String BASE_URL_PROD = "http://104.198.175.166:8080/";

    private static EndPointService mInstance;
    private final String TAG = this.getClass().getSimpleName();
    private EndPointInterface mEndPoint;

    private Context mContext;

    private EndPointService(Context context) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        mEndPoint = generateEndPoint(builder, context);
        mContext = context;
    }

    public static EndPointService getInstance(Context context) {

        if (mInstance == null) {
            mInstance = new EndPointService(context);
        }
        return mInstance;
    }

    private EndPointInterface generateEndPoint(OkHttpClient.Builder builder, final Context context) {

        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request.Builder ongoing = chain.request().newBuilder();
                ongoing.addHeader("Accept", "application/json");
                ongoing.addHeader("Content-Type", "application/json");
                ongoing.addHeader("Accept-Type", "json");
                if (isUserLoggedIn()) {
                    SharedPreferences sp = PreferenceManager
                            .getDefaultSharedPreferences(context);
                    ongoing.addHeader("Authorization", "Bearer " + sp.getString(Commons.SHARED_PREFERENCE_TOKEN, ""));
                    Log.d(TAG, "user token: " + sp.getString(Commons.SHARED_PREFERENCE_TOKEN, ""));
                }
                return chain.proceed(ongoing.build());
            }
        });

        OkHttpClient httpClient = builder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(EndPointService.BASE_URL_DEV)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();

        return retrofit.create(EndPointInterface.class);
    }

    public EndPointInterface getEndPoint() {
        return mEndPoint;
    }

    public boolean isUserLoggedIn() {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(mContext);

        return !sp.getString(Commons.SHARED_PREFERENCE_TOKEN, "").equals("");
    }
}
