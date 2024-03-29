import java.util.List;

public interface IDataAdapter {

    public static final int CONNECTION_OPEN_OK = 100;
    public static final int CONNECTION_OPEN_FAILED = 101;

    public static final int CONNECTION_CLOSE_OK = 200;
    public static final int CONNECTION_CLOSE_FAILED = 201;

    public static final int PRODUCT_SAVE_OK = 0;
    public static final int PRODUCT_SAVE_FAILED = 1;

    public static final int CUSTOMER_SAVE_OK = 100;
    public static final int CUSTOMER_SAVE_FAILED = 101;

    public static final int PURCHASE_SAVE_OK = 500;
    public static final int PURCHASE_SAVE_FAILED = 501;

    public static final int USER_SAVE_OK = 700;
    public static final int USER_SAVE_FAILED = 701;

    public static final int USER_DELETE_OK = 800;
    public static final int USER_DELETE_FAILED = 801;

    public static final int PRODUCT_DELETE_OK = 900;
    public static final int PRODUCT_DELETE_FAILED = 901;

    public int connect(String dbfile);
    public int disconnect();

    public ProductModel loadProduct(int id);
    public int saveProduct(ProductModel model);

    public CustomerModel loadCustomer(int id);
//    public int saveCustomer(CustomerModel model);
//
//    public int loadPurchase(int id);
    public int savePurchase(PurchaseModel model);

    public PurchaseListModel loadPurchaseHistory(int customerID);
    public PurchaseListModel loadEntirePurchaseHistory();

    public ProductListModel searchProduct(String name, double minPrice, double maxPrice);

    public UserModel loadUser(String username);
    //public int saveUser(UserModel user);
}
