package com.tenz.hotchpotch.http;

import com.tenz.hotchpotch.app.AppApplication;
import com.tenz.hotchpotch.util.ResourceUtil;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Author: TenzLiu
 * Date: 2018-01-22 10:11
 * Description: RetrofitFactory
 */

public class RetrofitFactory {

    private static final int DEFAULT_TIMEOUT_READ = 20;
    private static final int DEFAULT_TIMEOUT_WRITE = 20;
    private static final int DEFAULT_TIMEOUT_CONNECT = 10;
    private static String sBaseUrl = "http://www.wanandroid.com/tools/mockapi/2164/";
    private static RetrofitFactory sRetrofitFactory;
    private static OkHttpClient sOkHttpClient;
    private static Retrofit sRetrofit;
    // 避免出现 HTTP 403 Forbidden，参考：http://stackoverflow.com/questions/13670692/403-forbidden-with-java-but-not-web-browser
    public static final String AVOID_HTTP403_FORBIDDEN = "User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11";


    /**
     * 构造方法私有化
     */
    private RetrofitFactory() {
        sOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT_CONNECT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT_READ, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT_WRITE, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .cache(new Cache(new File(AppApplication.getInstance()
                        .getCacheDir(),""),1024*1024*10))
                .addInterceptor(InterceptorUtil.getHeadInterceptor())
                .addInterceptor(InterceptorUtil.getLogInterceptor())
                .addInterceptor(InterceptorUtil.getCacheInterceptor())
                .addNetworkInterceptor(InterceptorUtil.getCacheInterceptor())
                .build();
        sRetrofit = new Retrofit.Builder()
                .baseUrl(sBaseUrl)
                .client(sOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    /**
     * 单例获取
     * @return
     */
    public static RetrofitFactory getInstance(){
        if(sRetrofitFactory == null){
            synchronized (ResourceUtil.class){
                if(sRetrofitFactory == null){
                    sRetrofitFactory = new RetrofitFactory();
                }
            }
        }
        return sRetrofitFactory;
    }

    /**
     * 创建API
     * @param tClass
     * @param <T>
     * @return
     */
    public <T> T createApi(Class<T> tClass){
        return sRetrofit.create(tClass);
    }

    /**
     * 创建API
     * @param baseUrl 多个BaseURL切换
     * @param tClass
     * @param <T>
     * @return
     */
    public <T> T createApi(String baseUrl, Class<T> tClass){
        sRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(sOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return sRetrofit.create(tClass);
    }

}
