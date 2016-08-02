package com.example.jacobkoger.dota2application.clients;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.example.jacobkoger.dota2application.CacheStrategy;
import com.example.jacobkoger.dota2application.Config;
import com.example.jacobkoger.dota2application.ResponseCallbacks.CallbackWrapper;
import com.example.jacobkoger.dota2application.ResponseCallbacks.GenericCallback;
import com.example.jacobkoger.dota2application.data.history.MHMatchHistory;
import com.example.jacobkoger.dota2application.interceptors.AccountIdInterceptor;
import com.example.jacobkoger.dota2application.interceptors.CachedInterceptor;
import com.example.jacobkoger.dota2application.services.RecentMatchService;

import java.io.File;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class LoggedInClient {


    private static final String CACHE_DIR = "HttpResponseCache";
    private static final long CACHE_SIZE = 10 * 1024 * 1024;
    private static volatile LoggedInClient sClient;
    private final CachedInterceptor mCacheceptor = new CachedInterceptor();
    private final Retrofit mService;

    private LoggedInClient(Context context) {
        final Interceptor mAccountceptor = new AccountIdInterceptor(context);

        final OkHttpClient client = new OkHttpClient.Builder()
                .cache(new Cache(new File(context.getCacheDir(), CACHE_DIR), CACHE_SIZE))
                .addInterceptor(mCacheceptor)
                .addInterceptor(mAccountceptor)
                .addNetworkInterceptor(mCacheceptor)
                .build();
        mService = new Retrofit.Builder()
                .client(client)
                .validateEagerly(true)
                .baseUrl(Config.STEAM_ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized LoggedInClient with(Context context) {
        if (sClient == null) {
            sClient = new LoggedInClient(context.getApplicationContext());
        }
        return sClient;
    }

    public static synchronized LoggedInClient with(Fragment fragment) {
        return with(fragment.getContext());
    }

    public LoggedInClient cacheStrategy(CacheStrategy cacheStrategy) {
        mCacheceptor.setCacheStrategy(cacheStrategy);
        return this;
    }

    public void enqueueMatchHistory(GenericCallback<MHMatchHistory> callback) {
        mService.create(RecentMatchService.class)
                .getMatchHistory()
                .enqueue(CallbackWrapper.wrap(callback));
    }
}