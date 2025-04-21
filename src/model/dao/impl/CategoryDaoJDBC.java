package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.CategoryDao;
import model.entities.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDaoJDBC implements CategoryDao {

    private final Connection conn;

    public CategoryDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Category obj) {
        PreparedStatement st = null;

        try {
            st = conn.prepareStatement("INSERT INTO category (Name) VALUES (?)", Statement.RETURN_GENERATED_KEYS);

            st.setString(1, obj.getName());

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
        }
    }

    @Override
    public void update(Category obj) {
        PreparedStatement st = null;

        try {
            st = conn.prepareStatement("UPDATE category SET Name = ? WHERE Id = ?;");

            st.setString(1, obj.getName());
            st.setInt(2, obj.getId());

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
            st = conn.prepareStatement("DELETE FROM category WHERE Id = ?");

            st.setInt(1, id);

            int rows = st.executeUpdate();

            if (rows == 0) {
                throw new DbException("ERROR: ID not found");
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public Category findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement("SELECT * FROM category WHERE Id = ?");
            st.setInt(1, id);
            rs = st.executeQuery();

            if (rs.next()) {
                Category ct = instantiateCategory(rs);
                return ct;
            }

            return null;

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    private Category instantiateCategory(ResultSet rs) throws SQLException {
        Category ct = new Category();
        ct.setId(rs.getInt("Id"));
        ct.setName(rs.getString("Name"));
        return ct;
    }

    @Override
    public List<Category> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement("SELECT * FROM category ORDER BY Name");

            rs = st.executeQuery();

            List<Category> categoryList = new ArrayList<>();

            while (rs.next()) {
                Category cat = instantiateCategory(rs);
                categoryList.add(cat);
            }

            return categoryList;

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }
}
