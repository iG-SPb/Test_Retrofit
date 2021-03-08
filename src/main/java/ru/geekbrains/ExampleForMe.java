package ru.geekbrains;

import com.github.javafaker.Faker;
import lombok.NonNull;
import ru.geekbrains.db.dao.CategoriesMapper;
import ru.geekbrains.db.dao.ProductsMapper;
import ru.geekbrains.db.model.Categories;
import ru.geekbrains.db.model.CategoriesExample;
import ru.geekbrains.db.model.Products;
import ru.geekbrains.db.model.ProductsExample;
import ru.geekbrains.util.DbUtils;

public class ExampleForMe {

    static Faker faker = new Faker();
    private static  String resource = "mybatisConfig.xml";

    public static void main(String[] args) {
        CategoriesMapper categoriesMapper = DbUtils.getCategoriesMapper();
        ProductsMapper productsMapper = DbUtils.getProductsMapper();

        long categoriesNumber = countCategoriesNumber(categoriesMapper);

//        deleteProductById(productsMapper);

        Categories newCategory = new Categories();
        newCategory.setTitle(faker.artist().name());
        long categoryNumber = (categoriesNumber + 1);
        newCategory.setId((int) categoryNumber);
        categoriesMapper.insert(newCategory);

        categoriesMapper.deleteByPrimaryKey(1);




        productsMapper.insert(new Products(faker.commerce().productName(), 7777, categoryNumber));
        productsMapper.insert(new Products(faker.commerce().productName(), 7117, categoryNumber));
    }

    private static long countCategoriesNumber(CategoriesMapper categoriesMapper) {
        long categoriesCount = categoriesMapper.countByExample(new CategoriesExample());
        System.out.println("Количество категорий: " + categoriesCount);
        return categoriesCount;
    }

    private static void deleteProductById(ProductsMapper productsMapper) {
        Products product3101 = productsMapper.selectByPrimaryKey(3200L);
        productsMapper.deleteByPrimaryKey(product3101.getId());
    }
}
