package ru.geekbrains.service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import ru.geekbrains.dto.Category;

public interface CategoryService {
    @GET("/market/api/v1/categories/{id}")
    Call<Category> getCategory(@Path("id") Integer id);
}
