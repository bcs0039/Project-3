
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLiteDataAdapter implements IDataAdapter {

    Connection conn = null;

    public int connect(String dbfile) {
        try {
            // db parameters
            String url = "jdbc:sqlite:" + dbfile;
            // create a connection to the database
            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return CONNECTION_OPEN_FAILED;
        }
        return CONNECTION_OPEN_OK;
    }

    @Override
    public int disconnect() {
        try {
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return CONNECTION_CLOSE_FAILED;
        }
        return CONNECTION_CLOSE_OK;
    }

    public ProductModel loadProduct(int productID) {
        ProductModel product = null;

        try {
            String sql = "SELECT ProductId, Name, Price, Quantity FROM Products WHERE ProductId = " + productID;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                product = new ProductModel();
                product.mProductID = rs.getInt("ProductId");
                product.mName = rs.getString("Name");
                product.mPrice = rs.getDouble("Price");
                product.mQuantity = rs.getDouble("Quantity");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return product;
    }

    public int saveProduct(ProductModel product) {
        try {
            Statement stmt = conn.createStatement();
            ProductModel p = loadProduct(product.mProductID); // check if this product exists
            if (p != null) {
                stmt.executeUpdate("DELETE FROM Products WHERE ProductID = " + product.mProductID);
        }

            String sql = "INSERT INTO Products(ProductId, Name, Price, Quantity) VALUES " + product;
            System.out.println(sql);

            stmt.executeUpdate(sql);

        } catch (Exception e) {
            String msg = e.getMessage();
            System.out.println(msg);
            if (msg.contains("UNIQUE constraint failed"))
                return PRODUCT_SAVE_FAILED;
        }

        return PRODUCT_SAVE_OK;
    }

    public int deleteProduct(ProductModel product) {
        try {
            Statement stmt = conn.createStatement();
            ProductModel p = loadProduct(product.mProductID);
            if (p != null) {
                stmt.executeUpdate("DELETE FROM Products WHERE ProductID = " + p.mProductID);
            }

        } catch (Exception e) {
            String msg = e.getMessage();
            System.out.println(msg);
            if (msg.contains("UNIQUE constraint failed"))
                return PRODUCT_DELETE_FAILED;
        }

        return PRODUCT_DELETE_OK;
    }

    public PurchaseModel loadPurchase(int purchaseID) {
        PurchaseModel purchase = null;

        try {
            String sql = "SELECT PurchaseId, CustomerID, ProductId, Price, Quantity, Cost, Tax, Total, Date FROM Purchases WHERE PurchaseId = " + purchaseID;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                purchase = new PurchaseModel();
                purchase.mCustomerID = rs.getInt("CustomerId");
                purchase.mProductID = rs.getInt("ProductId");
                purchase.mPrice = rs.getDouble("Price");
                purchase.mQuantity = rs.getDouble("Quantity");
                purchase.mCost = rs.getDouble("Cost");
                purchase.mTax = rs.getDouble("Tax");
                purchase.mTotal = rs.getDouble("Total");
                purchase.mDate = rs.getString("Date");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return purchase;
    }


    public int savePurchase(PurchaseModel purchase) {
        try {
            Statement stmt = conn.createStatement();
            PurchaseModel p = loadPurchase(purchase.mPurchaseID); // check if this product exists
            if (p != null) {
                stmt.executeUpdate("DELETE FROM Purchases WHERE PurchaseID = " + purchase.mPurchaseID);
            }

            String sql = "INSERT INTO Purchases(PurchaseID, CustomerID, ProductId, Price, Quantity, Cost, Tax, Total, Date) VALUES " + purchase;
            System.out.println(sql);

            stmt.executeUpdate(sql);

        } catch (Exception e) {
            String msg = e.getMessage();
            System.out.println(msg);
            if (msg.contains("UNIQUE constraint failed"))
                return PURCHASE_SAVE_FAILED;
        }

        return PURCHASE_SAVE_OK;
    }

    @Override
    public PurchaseListModel loadPurchaseHistory(int id) {
        PurchaseListModel res = new PurchaseListModel();
        try {
            String sql = "SELECT * FROM Purchases WHERE CustomerId = " + id;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                PurchaseModel purchase = new PurchaseModel();
                purchase.mCustomerID = id;
                purchase.mPurchaseID = rs.getInt("PurchaseID");
                purchase.mProductID = rs.getInt("ProductID");
                purchase.mPrice = rs.getDouble("Price");
                purchase.mQuantity = rs.getDouble("Quantity");
                purchase.mCost = rs.getDouble("Cost");
                purchase.mTax = rs.getDouble("Tax");
                purchase.mTotal = rs.getDouble("Total");
                purchase.mDate = rs.getString("Date");

                res.purchases.add(purchase);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return res;
    }

    public PurchaseListModel loadEntirePurchaseHistory() {
        PurchaseListModel res = new PurchaseListModel();
        try {
            String sql = "SELECT * FROM Purchases";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                PurchaseModel purchase = new PurchaseModel();
                purchase.mCustomerID = rs.getInt("CustomerID");
                purchase.mPurchaseID = rs.getInt("PurchaseID");
                purchase.mProductID = rs.getInt("ProductID");
                purchase.mPrice = rs.getDouble("Price");
                purchase.mQuantity = rs.getDouble("Quantity");
                purchase.mCost = rs.getDouble("Cost");
                purchase.mTax = rs.getDouble("Tax");
                purchase.mTotal = rs.getDouble("Total");
                purchase.mDate = rs.getString("Date");

                res.purchases.add(purchase);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return res;
    }


    @Override
    public ProductListModel searchProduct(String name, double minPrice, double maxPrice) {
        ProductListModel res = new ProductListModel();
        try {
            String sql = "SELECT * FROM Products WHERE Name LIKE \'%" + name + "%\' "
                    + "AND Price >= " + minPrice + " AND Price <= " + maxPrice;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                ProductModel product = new ProductModel();
                product.mProductID = rs.getInt("ProductID");
                product.mName = rs.getString("Name");
                product.mPrice = rs.getDouble("Price");
                product.mQuantity = rs.getDouble("Quantity");
                res.products.add(product);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public CustomerModel loadCustomer(int id) {
        CustomerModel customer = null;

        try {
            String sql = "SELECT * FROM Customers WHERE CustomerId = " + id;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                customer = new CustomerModel();
                customer.mCustomerID = id;
                customer.mName = rs.getString("Name");
                customer.mPhone = rs.getString("Phone");
                customer.mAddress = rs.getString("Address");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return customer;
    }

    public int saveCustomer(CustomerModel customer) {
        try {
            Statement stmt = conn.createStatement();
            CustomerModel c = loadCustomer(customer.mCustomerID); // check if this product exists
            if (c != null) {
                stmt.executeUpdate("DELETE FROM Customers WHERE CustomerID = " + customer.mCustomerID);
            }

            String sql = "INSERT INTO Customers(CustomerId, Name, Phone, Address) VALUES " + customer;
            System.out.println(sql);

            stmt.executeUpdate(sql);

        } catch (Exception e) {
            String msg = e.getMessage();
            System.out.println(msg);
            if (msg.contains("UNIQUE constraint failed"))
                return CUSTOMER_SAVE_FAILED;
        }

        return CUSTOMER_SAVE_OK;

    }

    public UserModel loadUser(String username) {
        UserModel user = null;

        try {
            String sql = "SELECT * FROM Users WHERE Username = \"" + username + "\"";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                user = new UserModel();
                user.mUsername = username;
                user.mPassword = rs.getString("Password");
                user.mFullname = rs.getString("Fullname");
                user.mUserType = rs.getInt("Usertype");
                if (user.mUserType == UserModel.CUSTOMER)
                    user.mCustomerID = rs.getInt("CustomerID");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return user;
    }

    public int saveUser(UserModel user) {
        try {
            Statement stmt = conn.createStatement();
            UserModel u = loadUser(user.mUsername);
            if (u != null) {
                stmt.executeUpdate("DELETE FROM Users WHERE Username = \"" + user.mUsername + "\"");
            }

            String sql = "INSERT INTO Users(Username, Password, Fullname, UserType) VALUES " + user;
            System.out.println(sql);

            stmt.executeUpdate(sql);

        } catch (Exception e) {
            String msg = e.getMessage();
            System.out.println(msg);
            if (msg.contains("UNIQUE constraint failed"))
                return USER_SAVE_FAILED;
        }

        return USER_SAVE_OK;
    }

    public int deleteUser(UserModel user) {
        try {
            Statement stmt = conn.createStatement();
            UserModel u = loadUser(user.mUsername);
            if (u != null) {
                stmt.executeUpdate("DELETE FROM Users WHERE Username = \"" + user.mUsername + "\"");
            }

        } catch (Exception e) {
            String msg = e.getMessage();
            System.out.println(msg);
            if (msg.contains("UNIQUE constraint failed"))
                return USER_DELETE_FAILED;
        }

        return USER_DELETE_OK;
    }

}
