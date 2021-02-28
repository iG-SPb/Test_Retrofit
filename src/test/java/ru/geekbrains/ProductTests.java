package ru.geekbrains;

import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import ru.geekbrains.base.enums.CategoryType;
import ru.geekbrains.dto.Product;
import ru.geekbrains.service.ProductService;
import ru.geekbrains.util.RetrofitUtils;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.geekbrains.util.ConfigUtils.*;

public class ProductTests {
    Integer productId = baseProductId;
    Faker faker = new Faker();
    public static Boolean deleteFlag;
    static ProductService productService;
    Product product;


    @SneakyThrows
    @BeforeAll
    static void beforeAll() {
        productService = RetrofitUtils.getRetrofit().create(ProductService.class);
    }

    @BeforeEach
    void setUp() {
        product = new Product()
                .withTitle(faker.food().ingredient())
                .withPrice(faker.random().nextInt(minNegId, maxNegId));
                //.withCategoryTitle(CategoryType.FOOD.getTitle());
    }

    @SneakyThrows
    @Test
    void getProductTest() {
        deleteFlag = Boolean.FALSE;
        //retrofit2.Response<Product> response = productService.getProduct().execute();
        //assertThat(response.isSuccessful()).isTrue();
    }

    @SneakyThrows
    @Test
    void getProductIdPositiveTest() {
        deleteFlag = Boolean.FALSE;
        retrofit2.Response<Product> response = productService.getProductId(baseProductId).execute();
        assertThat(response.isSuccessful()).isTrue();
    }

    @SneakyThrows
    @Test
    void getProductIdNegativeTest() {
        deleteFlag = Boolean.FALSE;
        Integer tmpId = faker.random().nextInt(minNegId, maxNegId);
        retrofit2.Response<Product> response = productService.getProductId(tmpId).execute();
        assertThat(response.code()).isEqualTo(404);
    }

    @SneakyThrows
    @ParameterizedTest
    @EnumSource(CategoryType.class)
    void createNewProductPositiveTest(CategoryType categoryType) {
        deleteFlag = Boolean.TRUE;
        retrofit2.Response<Product> response =
                productService.createProduct(product.withCategoryTitle(categoryType.getTitle()))
                        .execute();
        assert response.body() != null;
        productId = response.body().getId();
        System.out.println("productId - " + productId);
        assertThat(response.isSuccessful()).isTrue();
    }

    @SneakyThrows
    @ParameterizedTest
    @EnumSource(CategoryType.class)
    void modifyProductPositiveTest(CategoryType categoryType) {
        deleteFlag = Boolean.FALSE;
        productId = baseProductId;
        retrofit2.Response<Product> response = productService
                .modifyProduct(product.withCategoryTitle(categoryType.getTitle()).withId(productId))
                .execute();
        assertThat(response.isSuccessful()).isTrue();
    }

    @SneakyThrows
    @ParameterizedTest
    @EnumSource(CategoryType.class)
    void modifyProductNegativeTest(CategoryType categoryType) {
        deleteFlag = Boolean.FALSE;
        Integer tmpId = faker.random().nextInt(minNegId, maxNegId);
        retrofit2.Response<Product> response = productService
                .modifyProduct(product.withCategoryTitle(categoryType.getTitle()).withId(tmpId))
                .execute();
        assert response.body() == null;
        assertThat(response.code()).isEqualTo(400);
    }

    @SneakyThrows
    @Test
    void createNewProductNegativeTest() {
        deleteFlag = Boolean.FALSE;
        Integer tmpId = faker.random().nextInt(minNegId, maxNegId);
        retrofit2.Response<Product> response =
                productService.createProduct(product.withId(tmpId))
                        .execute();
        assert response.body() == null;
        assertThat(response.code()).isEqualTo(400);
    }

    @SneakyThrows
    @Test
    void deleteNegativeTest() {
        deleteFlag = Boolean.FALSE;
        Integer tmpId = faker.random().nextInt(minNegId, maxNegId);
        try {
            retrofit2.Response<ResponseBody> response =
                productService.deleteProduct(tmpId).execute();
            assert response.body() == null;
            assertThat(response.code()).isEqualTo(500);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    @AfterEach
    void tearDown() {
        if (deleteFlag) {
            try {
                retrofit2.Response<ResponseBody> response =
                        productService.deleteProduct(productId)
                                .execute();
                assertThat(response.code()).isEqualTo(200);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
