package hanu.a2_1901040191.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.List;

import hanu.a2_1901040191.models.CartItem;
import hanu.a2_1901040191.models.Product;

public class EntitiesManager {
    private static EntitiesManager instance;
    private DbHelper dbHelper;
    private SQLiteDatabase db;

    private final ProductManager productManager = new ProductManager();
    private final CartManager cartManager = new CartManager();

    private EntitiesManager() {
    }

    private EntitiesManager(Context context) {
        dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public static EntitiesManager getInstance(Context context) {
        if (instance == null) {
            instance = new EntitiesManager(context);
        }
        return instance;
    }

    public void checkVersion() {
        Log.i("db", "//////db version: " + db.getVersion());
        if (db.getVersion() < DbHelper.DB_VERSION) {
            dbHelper.onUpgrade(db, db.getVersion(),  DbHelper.DB_VERSION);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if (db != null) {
            db.close();
        }
        if (dbHelper != null) {
            dbHelper.close();
        }
    }

    /**
     * @effects return productManager
     */
    public ProductManager getProductManager() {
        return productManager;
    }

    /**
     * @effects return cartManager
     */
    public CartManager getCartManager() {
        return cartManager;
    }

    public final class ProductManager {
        private static final String INSERT_STMT = "INSERT INTO " + DbSchema.ProductTable.NAME + " ("
                + DbSchema.ProductTable.Cols.ID + ", "
                + DbSchema.ProductTable.Cols.THUMBNAIL + ", "
                + DbSchema.ProductTable.Cols.NAME + ", "
                + DbSchema.ProductTable.Cols.PRICE
                + ") VALUES (?, ?, ?, ?)";

        private static final String UPDATE_STMT = "UPDATE " + DbSchema.ProductTable.NAME + " SET "
                + DbSchema.ProductTable.Cols.THUMBNAIL + " = ?, "
                + DbSchema.ProductTable.Cols.NAME + " = ?, "
                + DbSchema.ProductTable.Cols.PRICE + " = ? "
                + "WHERE " + DbSchema.ProductTable.Cols.ID + " = ?";

        private static final String DELETE_ALL = "DELETE FROM " + DbSchema.ProductTable.NAME;

        private ProductManager() {
        }

        public List<Product> all() {
            String sql = "SELECT * FROM " + DbSchema.ProductTable.NAME;
            Cursor cursor = db.rawQuery(sql, null);
            ProductCursorWrapper productCursorWrapper = new ProductCursorWrapper(cursor);
            return productCursorWrapper.getProducts();
        }

        public boolean add(Product product) {
            SQLiteStatement statement = db.compileStatement(INSERT_STMT);
            statement.bindString(1, product.getId() + "");
            statement.bindString(2, product.getThumbnail());
            statement.bindString(3, product.getName());
            statement.bindString(4, product.getUnitPrice() + "");

            long result = statement.executeInsert();
            return result > 0;
        }

        public boolean delete(long id) {
            int result = db.delete(DbSchema.ProductTable.NAME, DbSchema.ProductTable.Cols.ID + " = ?", new String[]{id + ""});
            return result > 0;
        }

        public boolean update(Product product) {
            SQLiteStatement statement = db.compileStatement(UPDATE_STMT);
            statement.bindString(1, product.getThumbnail());
            statement.bindString(2, product.getName());
            statement.bindString(3, product.getUnitPrice() + "");
            statement.bindString(4, product.getId() + "");

            long result = statement.executeUpdateDelete();
            return result > 0;
        }

        public void clear() {
            db.execSQL(DELETE_ALL);
        }
    }

    public final class CartManager {
        private static final String INSERT_STMT = "INSERT INTO " + DbSchema.CartItemTable.NAME + " ("
                + DbSchema.CartItemTable.Cols.PRODUCT_ID + ", "
                + DbSchema.CartItemTable.Cols.QUANTITY
                + ") VALUES (?, ?)";

        private static final String UPDATE_STMT = "UPDATE " + DbSchema.CartItemTable.NAME + " SET "
                + DbSchema.CartItemTable.Cols.PRODUCT_ID + " = ?, "
                + DbSchema.CartItemTable.Cols.QUANTITY + " = ? "
                + "WHERE " + DbSchema.CartItemTable.Cols.ID + " = ?";

        private static final String DELETE_ALL = "DELETE FROM " + DbSchema.CartItemTable.NAME;

        private CartManager() {
        }

        public List<CartItem> all() {
            String sql = "SELECT * FROM " + DbSchema.CartItemTable.NAME;
            Cursor cursor = db.rawQuery(sql, null);
            CartItemCursorWrapper cartItemCursorWrapper = new CartItemCursorWrapper(cursor);
            return cartItemCursorWrapper.getCarts();
        }

        public boolean add(CartItem cartItem) {
            SQLiteStatement statement = db.compileStatement(INSERT_STMT);
            statement.bindString(1, cartItem.getProductId() + "");
            statement.bindString(2, cartItem.getQuantity() + "");

            long result = statement.executeInsert();
            return result > 0;
        }

        public boolean delete(long id) {
            int result = db.delete(DbSchema.CartItemTable.NAME, DbSchema.CartItemTable.Cols.ID + "= ?", new String[]{id + ""});
            return result > 0;
        }

        public boolean update(CartItem cartItem) {
            SQLiteStatement statement = db.compileStatement(UPDATE_STMT);
            statement.bindString(1, cartItem.getProductId() + "");
            statement.bindString(2, cartItem.getQuantity() + "");
            statement.bindString(3, cartItem.getId() + "");

            long result = statement.executeUpdateDelete();
            return result > 0;
        }

        public void clear() {
            db.execSQL(DELETE_ALL);
        }
    }
}
