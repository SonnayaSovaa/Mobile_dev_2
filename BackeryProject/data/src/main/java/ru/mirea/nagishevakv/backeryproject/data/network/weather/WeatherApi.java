package ru.mirea.nagishevakv.backeryproject.data.network.weather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApi {
    @GET("weather")
    Call<WeatherResponse> getWeather(
        @Query("q") String city,
        @Query("appid") String apiKey,
        @Query("units") String units,
        @Query("lang") String lang
    );
}
