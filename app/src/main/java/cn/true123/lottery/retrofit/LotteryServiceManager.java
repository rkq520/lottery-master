package cn.true123.lottery.retrofit;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import cn.true123.lottery.application.LotteryApplication;
import cn.true123.lottery.model.LotteryConstant;
import cn.true123.lottery.model.Lottery;
import cn.true123.lottery.utils.LotteryUtils;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by feimeng0530 on 2016/3/17.
 */
public class LotteryServiceManager {
    public static LotteryServiceManager instance;
    private OkHttpClient client;
    private LotteryService service;

    private Retrofit retrofit;
    private LotteryApiService apiService;

    private Retrofit apiRetrofit;
    private Context context;
    private static String TAG = "LotteryServiceManager";

    private LotteryServiceManager(Context context) {
        this.context = context;
        init();
    }

    /**
     * return singleton LotteryServiceManager instance
     *
     * @return instance
     */
    public static LotteryServiceManager instance(Context context) {
        if (instance == null) {
            synchronized (LotteryServiceManager.class) {
                if (instance == null) {
                    instance = new LotteryServiceManager(context);
                }
            }
        }
        return instance;
    }

    /**
     * initialize the OKHttpClient and retrofit
     */
    private void init() {
        client = new OkHttpClient.Builder().connectTimeout(3000, TimeUnit.MILLISECONDS).cache(new Cache(context.getExternalCacheDir(), ((LotteryApplication) ((Activity) context).getApplication()).getCacheSize())).build();

        retrofit = new Retrofit.Builder()
                .baseUrl(LotteryConstant.RETROFIT_BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        service = retrofit.create(LotteryService.class);
        apiRetrofit = new Retrofit.Builder()
                .baseUrl(LotteryConstant.RETROFIT_API_VERSION_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        apiService = apiRetrofit.create(LotteryApiService.class);
    }
    public void getLastVersion(Subscriber subscriber,String token) {
        apiService.getLastVersion(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
    /**
     * The method is used to get the last data for 360 lottery
     *
     * @param subscriber
     */
    public void getLastData360(Subscriber subscriber) {

        service.geLastData360()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<Lottery, List>() {
                    @Override
                    public List call(Lottery lottery) {
                        return getLotteryList(lottery);
                    }
                }).subscribe(subscriber);
    }

    /**
     * @param lottery
     * @return
     */
    private List<Lottery.IEntity> getLotteryList(Lottery lottery) {
        Log.i(TAG, lottery.toString());
        List ret = new ArrayList();
        Map<String, String> available = LotteryUtils.getAllAvailable();
        Set keySet = available.keySet();
        List<Lottery.Entity> entity = lottery.getAllEntities();
        for (Lottery.Entity e : entity) {
            Log.i(TAG, "Lottery.Entity=" + (e == null ? "null" : e.getLotName()));
            if (e != null && keySet.contains(e.getLotName())) {
                ret.add(e);
            }
        }
        return ret;
    }

    /**
     * Get the detail for one issue of lottery
     *
     * @param subscriber
     * @param lotId
     * @param issue
     */
    public void getLotteryDetail(Subscriber subscriber, String lotId, String issue) {

        service.getLotteryDetail(lotId, issue)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * @param subscriber
     * @param lotId
     */
    public void getHistory360(Subscriber subscriber, String lotId, String page) {

        service.geLotteryHistory(lotId, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public static void main(String[] args) {
        instance(null).getLastData360(new Subscriber<Lottery>() {
            @Override
            public void onCompleted() {
                System.out.println("onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("onError" + e.getLocalizedMessage());
            }

            @Override
            public void onNext(Lottery o) {
                System.out.println(o.getValue220028());
            }
        });
    }

}
