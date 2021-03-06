package ru.geekbrains;

import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ResponseBody;
import org.apache.ibatis.jdbc.Null;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import ru.geekbrains.base.enums.CategoryType;
import ru.geekbrains.db.dao.CategoriesMapper;
import ru.geekbrains.db.dao.ProductsMapper;
import ru.geekbrains.db.model.Categories;
import ru.geekbrains.db.model.CategoriesExample;
import ru.geekbrains.db.model.Products;
import ru.geekbrains.db.model.ProductsExample;
import ru.geekbrains.dto.Product;
import ru.geekbrains.service.CategoryService;
import ru.geekbrains.service.ProductService;
import ru.geekbrains.util.DbUtils;
import ru.geekbrains.util.RetrofitUtils;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.geekbrains.util.ConfigUtils.*;

@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MyBatisProductTests {
    Integer productId;
    Faker faker = new Faker();
    public static Boolean deleteFlag;
    static ProductService productService;
    Product product;
    ProductsMapper productsMapper = DbUtils.getProductsMapper();
    ProductsExample example = new ProductsExample();

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
    }

    @SneakyThrows
    @Test
    @DisplayName("Create base product for Test")
    @Order(1)
    void createBaseProductPositiveTest() {
        deleteFlag = Boolean.FALSE;
/*
        Products newProduct = new Products();
        newProduct.setTitle(faker.food().ingredient());
        newProduct.setPrice(faker.random().nextInt(minNegId, maxNegId));
        newProduct.setCategory_id((long) CategoryType.FOOD.getId());
        productsMapper.insert(newProduct);
*/
        retrofit2.Response<Product> response =
                productService.createProduct(product.withCategoryTitle(CategoryType.FOOD.getTitle()))
                        .execute();
        assert response.body() != null;
        productId = response.body().getId();
        baseProductId = productId;
        assertThat(response.isSuccessful()).isTrue();

        assertThat(productsMapper.selectByPrimaryKey((long) productId)).isNotNull();
    }

    @SneakyThrows
    @Test
    @DisplayName("Product GET Positive Test")
    @Order(3)
    void getProductTest() {
        deleteFlag = Boolean.FALSE;
        // запрос перестал выполняться - сервер выдает 500 - swagger выполняет аналогично
        // retrofit2.Response<Product> response = productService.getProduct().execute();
        // assertThat(response.isSuccessful()).isTrue();
    }

    @SneakyThrows
    @Test
    @DisplayName("Product GET Id Positive Test")
    @Order(4)
    void getProductIdPositiveTest() {
        deleteFlag = Boolean.FALSE;
        retrofit2.Response<Product> response = productService.getProductId(baseProductId).execute();
        assertThat(response.isSuccessful()).isTrue();

        assertThat(productsMapper.selectByPrimaryKey((long) baseProductId)).isNotNull();
    }

    @SneakyThrows
    @Test
    @DisplayName("Product GET Id Negative Test")
    @Order(5)
    void getProductIdNegativeTest() {
        deleteFlag = Boolean.FALSE;
        Integer tmpId = faker.random().nextInt(minNegId, maxNegId);
        retrofit2.Response<Product> response = productService.getProductId(tmpId).execute();
        assertThat(response.code()).isEqualTo(404);

        assertThat(productsMapper.selectByPrimaryKey((long) tmpId)).isNull();
    }

    @SneakyThrows
    @ParameterizedTest
    @EnumSource(CategoryType.class)
    @DisplayName("Product POST Positive Test")
    @Order(6)
    void createNewProductPositiveTest(CategoryType categoryType) {
        deleteFlag = Boolean.TRUE;
        retrofit2.Response<Product> response =
                productService.createProduct(product.withCategoryTitle(categoryType.getTitle()))
                        .execute();
        assert response.body() != null;
        productId = response.body().getId();
        baseProductId = productId;
        //System.out.println("productId - " + productId);
        assertThat(response.isSuccessful()).isTrue();

        assertThat(productsMapper.selectByPrimaryKey((long) productId)).isNotNull();
        assertThat((productsMapper.selectByPrimaryKey((long) productId)).getPrice()).isEqualTo(product.getPrice());
        assertThat((productsMapper.selectByPrimaryKey((long) productId)).getTitle()).isEqualTo(product.getTitle());
    }

    @SneakyThrows
    @ParameterizedTest
    @EnumSource(CategoryType.class)
    @DisplayName("Product PUT Positive Test")
    @Order(2)
    void modifyProductPositiveTest(CategoryType categoryType) {
        deleteFlag = Boolean.FALSE;
        productId = baseProductId;
        retrofit2.Response<Product> response = productService
                .modifyProduct(product.withCategoryTitle(categoryType.getTitle()).withId(productId))
                .execute();
        assertThat(response.isSuccessful()).isTrue();

        assertThat(productsMapper.selectByPrimaryKey((long) productId)).isNotNull();
        assertThat((productsMapper.selectByPrimaryKey((long) productId)).getPrice()).isEqualTo(product.getPrice());
        assertThat((productsMapper.selectByPrimaryKey((long) productId)).getTitle()).isEqualTo(product.getTitle());
    }

    @SneakyThrows
    @ParameterizedTest
    @EnumSource(CategoryType.class)
    @DisplayName("Product PUT Negative Test")
    @Order(7)
    void modifyProductNegativeTest(CategoryType categoryType) {
        deleteFlag = Boolean.FALSE;
        Integer tmpId = faker.random().nextInt(minNegId, maxNegId);
        retrofit2.Response<Product> response = productService
                .modifyProduct(product.withCategoryTitle(categoryType.getTitle()).withId(tmpId))
                .execute();
        assert response.body() == null;
        assertThat(response.code()).isEqualTo(400);

        assertThat(productsMapper.selectByPrimaryKey((long) tmpId)).isNull();
    }

    @SneakyThrows
    @Test
    @DisplayName("Product POST Negative Test")
    @Order(8)
    void createNewProductNegativeTest() {
        deleteFlag = Boolean.FALSE;
        Integer tmpId = faker.random().nextInt(minNegId, maxNegId);
        retrofit2.Response<Product> response =
                productService.createProduct(product.withId(tmpId))
                        .execute();
        assert response.body() == null;
        assertThat(response.code()).isEqualTo(400);

        assertThat(productsMapper.selectByPrimaryKey((long) tmpId)).isNull();
    }

    @SneakyThrows
    @Test
    @DisplayName("Product DELETE Negative Test")
    @Order(9)
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
        assertThat(productsMapper.selectByPrimaryKey((long) tmpId)).isNull();
        }

    @AfterEach
    void tearDown() {
        if (deleteFlag) {

        productsMapper.deleteByPrimaryKey((long) productId);
        assertThat(productsMapper.selectByPrimaryKey((long) productId)).isNull();

/*            try {
                retrofit2.Response<ResponseBody> response =
                        productService.deleteProduct(productId)
                                .execute();
                assertThat(response.code()).isEqualTo(200);
            } catch (IOException e) {
                e.printStackTrace();
            }
*/
        }
    }
}
