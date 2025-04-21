package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.ProductDao;
import model.entities.Category;
import model.entities.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductDaoJDBC implements ProductDao {

    private final Connection conn;

    public ProductDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Product obj) {
        PreparedStatement st = null;

        try {
            st = conn.prepareStatement("INSERT INTO product (Name, Price, CategoryId) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

            st.setString(1, obj.getName());
            st.setDouble(2, obj.getPrice());
            st.setInt(3, obj.getCategory().getId());

            int rowsAffected = st.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    obj.setId(id);
                }
                DB.closeResultSet(rs);
            } else {
                throw new DbException("Unexpected error! No rows affected!");
            }

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void update(Product obj) {

        PreparedStatement st = null;

        try {
            st = conn.prepareStatement("UPDATE product " + "SET Name = ?, Price = ?, CategoryId = ? " + "WHERE Id = ?");

            st.setString(1, obj.getName());
            st.setDouble(2, obj.getPrice());
            st.setInt(3, obj.getCategory().getId());
            st.setInt(4, obj.getId());

            st.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement st = null;

        try {
            st = conn.prepareStatement("DELETE FROM product WHERE Id = ?;");

            st.setInt(1, id);

            int rows = st.executeUpdate();

            if (rows == 0){
                throw new DbException("ERROR: ID not found");
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public Product findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement("SELECT p.*, c.Name AS CatName " + "FROM product p " + "INNER JOIN category c ON p.CategoryId = c.Id " + "WHERE p.Id = ?;");

            st.setInt(1, id);
            rs = st.executeQuery();
            if (rs.next()) {
                Category ct = instantiateCategory(rs);
                Product obj = instantiateProduct(rs, ct);
                return obj;
            }

            return null;


        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    private Product instantiateProduct(ResultSet rs, Category ct) throws SQLException {
        Product obj = new Product();
        obj.setId(rs.getInt("Id"));
        obj.setName((rs.getString("Name")));
        obj.setPrice(rs.getDouble("Price"));
        obj.setCategory(ct);
        return obj;
    }

    private Category instantiateCategory(ResultSet rs) throws SQLException {
        Category ct = new Category();
        ct.setId(rs.getInt("Id"));
        ct.setName(rs.getString("Name"));
        return ct;
    }

    @Override
    public List<Product> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement("SELECT p.*, c.Name AS CatName " + "FROM product p " + "INNER JOIN category c ON p.CategoryId = c.Id " + "ORDER BY p.Name;");

            rs = st.executeQuery();

            List<Product> productList = new ArrayList<>();
            Map<Integer, Category> categoryMap = new HashMap<>();

            while (rs.next()) {
                Category cat = categoryMap.get(rs.getInt("CategoryId"));

                if (cat == null) {
                    cat = instantiateCategory(rs);
                    categoryMap.put(rs.getInt("CategoryId"), cat);
                }

                Product obj = instantiateProduct(rs, cat);
                productList.add(obj);
            }
            return productList;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Product> findByCategory(Category category) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement("SELECT p.*, c.Name AS CatName " + "FROM product p " + "INNER JOIN category c ON p.CategoryId = c.Id " + "WHERE c.Id = ? " + "ORDER BY p.Name;");

            st.setInt(1, category.getId());
            rs = st.executeQuery();

            List<Product> productList = new ArrayList<>();
            Map<Integer, Category> categoryMap = new HashMap<>();

            while (rs.next()) {
                Category cat = categoryMap.get(rs.getInt("CategoryId"));

                if (cat == null) {
                    cat = instantiateCategory(rs);
                    categoryMap.put(rs.getInt("CategoryId"), cat);
                }

                Product obj = instantiateProduct(rs, cat);
                productList.add(obj);
            }
            return productList;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }
}
