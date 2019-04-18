package com.stone.baselib.net.example;

import java.util.Map;

import io.reactivex.Flowable;
import retrofit2.http.FieldMap;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Stone
 * 2019/4/4
 **/
public interface SService {
    @POST("S/T/One")
    Flowable<StoneModel> postStone(@FieldMap Map<String, Object> params);

    @GET("S/T/One")
    Flowable<StoneModel> getStone(@Query("stone") String stone);

}
