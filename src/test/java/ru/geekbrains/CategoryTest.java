package ru.geekbrains;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import retrofit2.Response;
import retrofit2.Retrofit;
import ru.geekbrains.dto.Category;
import ru.geekbrains.service.CategoryService;
import ru.geekbrains.util.RetrofitUtils;

import java.io.IOException;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.geekbrains.base.enums.CategoryType.FOOD;

public class CategoryTest {
    static CategoryService categoryService;

    @BeforeAll
    static void beforeAll() {
        categoryService = RetrofitUtils.getRetrofit().create(CategoryService.class);
    }

    @Test
    void getCategoryPositiveTest() throws IOException {

        Response<Category> response = categoryService
                .getCategory(FOOD.getId())
                .execute();
        System.out.print(response.toString());
        assertThat(response.isSuccessful()).isTrue();
        assertThat(response.body().getId()).as("Response is Null").isEqualTo(1);
    }
}
