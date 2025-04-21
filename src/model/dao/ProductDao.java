package model.dao;

import model.entities.Category;
import model.entities.Product;

import java.util.List;

public interface ProductDao {
   void insert(Product obj);
   void update(Product obj);
   void deleteById(Integer id);
   Product findById(Integer id);
   List<Product> findAll();
   List<Product> findByCategory(Category category);
}
