package com.example.task.Data;

import com.example.task.Data.Models.CarsListModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetDataService {
    @GET("/api/v1/cars")
    Call<CarsListModel> getCarList(
            @Query("page") int page);
}
