package ru.mirea.nagishevakv.backeryproject.data.network.weather;

import com.google.gson.annotations.SerializedName;

public class WeatherResponse {
    @SerializedName("main")
    public Main main;
    @SerializedName("weather")
    public Weather[] weather;
    @SerializedName("name")
    public String name;

    public static class Main {
        @SerializedName("temp")
        public float temp;
        @SerializedName("feels_like")
        public float feels_like;
        @SerializedName("humidity")
        public int humidity;
    }

    public static class Weather {
        @SerializedName("description")
        public String description;
    }
}
