package model.dao;

import db.DB;
import model.dao.impl.CategoryDaoJDBC;
import model.dao.impl.ProductDaoJDBC;
import model.entities.Category;
import model.entities.Product;

public class DaoFactory {
    public  static ProductDao createProductDao(){
        return new ProductDaoJDBC(DB.getConnection());
    }

    public static CategoryDao createCategoryDao(){
        return  new CategoryDaoJDBC(DB.getConnection());
    }
}
