package com.example.example.http;

import com.example.example.model.BaseModel;
import com.example.example.model.NearMerChantsModel;
import com.example.example.model.StoneModel;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
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

    @FormUrlEncoded
    @POST("index.php?g=api&m=Business&a=businesslist")
    Flowable<NearMerChantsModel> queryNearMerChantsLocation(@FieldMap Map<String, Object> params);

    @GET("index.php?g=api&m=Business&a=businesslist")
    Flowable<NearMerChantsModel> getNearMerChantsLocation(@Query("uid") String uid,@Query("lat") String lat,@Query("lng") String lng);
}
