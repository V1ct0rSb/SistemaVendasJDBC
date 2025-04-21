package application;

import model.dao.CategoryDao;
import model.dao.DaoFactory;
import model.entities.Category;

import java.util.List;
import java.util.Scanner;

public class Program02 {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        CategoryDao categoryDao = DaoFactory.createCategoryDao();

        System.out.println("=== Test 1: Category -> FindById ===");
        Category category = categoryDao.findById(1);
        System.out.println(category);

        System.out.println("=== Test 2: Category -> FindAll ===");
        List<Category> list = categoryDao.findAll();
        for (Category obj : list) {
            System.out.println(obj);
        }

        System.out.println("=== Test 3: Category -> Insert ===");
        Category newCategory = new Category(null, "Esportes");
        categoryDao.insert(newCategory);
        System.out.println("Inserted! New Id = " + newCategory.getId());

        System.out.println("=== Test 4: Category -> Update ===");
        category = categoryDao.findById(1);
        category.setName("Eletrônicos Atualizado");
        categoryDao.update(category);
        System.out.println("Update Completed");

        System.out.println("=== Test 5: Category -> Delete ===");
        System.out.print("Enter Id for delete test: ");
        int id = sc.nextInt();
        categoryDao.deleteById(id);
        System.out.println("Delete Completed");

        sc.close();


    }
}
