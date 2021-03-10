package ru.geekbrains;

import com.github.javafaker.Faker;
import groovyjarjarantlr4.v4.parse.BlockSetTransformer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import retrofit2.Response;
import ru.geekbrains.base.enums.CategoryType;
import ru.geekbrains.db.dao.CategoriesMapper;
import ru.geekbrains.db.model.Categories;
import ru.geekbrains.db.model.CategoriesExample;
import ru.geekbrains.dto.Category;
import ru.geekbrains.service.CategoryService;
import ru.geekbrains.util.DbUtils;
import ru.geekbrains.util.RetrofitUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.geekbrains.util.ConfigUtils.maxNegId;
import static ru.geekbrains.util.ConfigUtils.minNegId;

@Slf4j
public class MyBatisCategoryTest {
    static CategoryService categoryService;
    CategoriesMapper categoriesMapper = DbUtils.getCategoriesMapper();
    CategoriesExample example = new CategoriesExample();
    static Faker faker = new Faker();

    @BeforeAll
    static void beforeAll() throws MalformedURLException {
        categoryService = RetrofitUtils.getRetrofit().create(CategoryService.class);
    }

    @ParameterizedTest
    @EnumSource(CategoryType.class)
    @DisplayName("Category Positive Test")
    //@Step("test Post positive")
    void getCategoryPositiveTest(CategoryType categoryType) throws IOException {
        Response<Category> response = categoryService
                .getCategory(categoryType.getId())
                .execute();
        assertThat(response.isSuccessful()).isTrue();
        assert response.body() != null;

        Integer mytestId = categoriesMapper.selectByPrimaryKey(categoryType.getId()).getId();
        assertThat(response.body().getId()).as("Response is 1 or 2").isEqualTo(mytestId);
    }

    @Test
    @DisplayName("Category Negative Test")
    void getCategoryNegativeTest() throws IOException {
        List<Categories> myLists = categoriesMapper.selectByExample(example);
        Integer tmpId = faker.random().nextInt(minNegId, maxNegId);

        Response<Category> response = categoryService
                .getCategory(tmpId)
                .execute();
        assert response.body() == null;

        Integer searchId = 0;
        for (Categories myList : myLists) {
            if (myList.getId() == tmpId) {
                searchId = 1;
                break;
            }
        }

        assertTrue(searchId.equals(0));
        assertThat(response.code()).as("Not response").isEqualTo(404);
    }
}
