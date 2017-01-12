package com.binx.fireline.retrofit.api;

import com.binx.fireline.model.Incident;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by james on 1/5/17.
 */

public interface ApiService {

    /*
        Retrofit get annotation with our URL
        And our method that will return us the List of ContactList
        */
    @GET("/data/fireline.json")
    Call<ArrayList<Incident>> getFirelineJSON();
}