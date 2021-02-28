package ru.geekbrains.util;

import lombok.experimental.UtilityClass;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.time.Duration;

@UtilityClass
public class RetrofitUtils {

    public Retrofit getRetrofit() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(Duration.ofSeconds(30L))
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();
        return new Builder()
            .baseUrl("http://80.78.248.82:8189")
            .addConverterFactory(JacksonConverterFactory.create())
            .build();
    }
}
