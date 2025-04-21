package application;

import model.dao.DaoFactory;
import model.dao.ProductDao;
import model.entities.Category;
import model.entities.Product;

import java.util.List;
import java.util.Scanner;

public class Program {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        ProductDao productDao = DaoFactory.createProductDao();

        System.out.println("=== Test Category: ===");
        Category category = new Category(1, "Eletronicos");
        System.out.println(category);

        System.out.println("=== Test Product: ===");
        Product product = new Product(1, "Geladeira", 3000.0, category);
        System.out.println(product);

        System.out.println(" ============================== ");

        System.out.println("=== Test Product -> FindById ===");
        product = productDao.findById(2);
        System.out.println(product);

        System.out.println("=== Test Product -> FindByCategory ===");
        Category category1 = new Category(1, null);
        List<Product> productList = productDao.findByCategory(category1);
        for (Product obj : productList) {
            System.out.println(obj);
        }

        System.out.println("=== Test Product -> FindAll ===");
        List<Product> productList1 = productDao.findAll();
        for (Product obj : productList1) {
            System.out.println(obj);
        }

        System.out.println("=== Test Product -> Update ===");
        product = productDao.findById(2);
        product.setName("Camisa Lacoste");
        productDao.update(product);
        System.out.println("Update Completed");

        System.out.println("=== Test Product -> Delete ===");
        System.out.println("Enter Id for delete test");
        int id = sc.nextInt();
        productDao.deleteById(id);
        System.out.println("Delete Completed");
    }
}
