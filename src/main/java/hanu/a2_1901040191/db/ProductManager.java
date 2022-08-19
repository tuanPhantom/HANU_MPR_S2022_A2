package hanu.a2_1901040191.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.List;

import hanu.a2_1901040191.models.Product;

public class ProductManager {
    private static ProductManager instance;
    private DbHelper dbHelper;
    private SQLiteDatabase db;

    private static final String INSERT_STMT = "INSERT INTO " + DbSchema.ProductTable.NAME + " ("
            + DbSchema.ProductTable.Cols.ID + ", "
            + DbSchema.ProductTable.Cols.THUMBNAIL + ", "
            + DbSchema.ProductTable.Cols.NAME + ", "
            + DbSchema.ProductTable.Cols.PRICE + ", "
            + DbSchema.ProductTable.Cols.QUANTITY
            + ") VALUES (?, ?, ?, ?, ?)";

    private static final String UPDATE_STMT = "UPDATE " + DbSchema.ProductTable.NAME + " SET "
            + DbSchema.ProductTable.Cols.THUMBNAIL + " = ?, "
            + DbSchema.ProductTable.Cols.NAME + " = ?, "
            + DbSchema.ProductTable.Cols.PRICE + " = ?, "
            + DbSchema.ProductTable.Cols.QUANTITY + " = ?"
            + "WHERE " + DbSchema.ProductTable.Cols.ID + " = ?";

    private static final String DELETE_ALL = "DELETE FROM " + DbSchema.ProductTable.NAME;

    private ProductManager() {
    }

    public static ProductManager getInstance(Context context) {
        if (instance == null) {
            instance = new ProductManager(context);
        }
        return instance;
    }

    public static ProductManager getInstance() {
        if (instance == null) {
            return null;
        }
        return instance;
    }

    private ProductManager(Context context) {
        dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public List<Product> all() {
//        List<Product> products = new ArrayList<>();
//        int idIndex = cursor.getColumnIndex(DbSchema.ProductTable.Cols.ID);
//        int thumbnailIndex = cursor.getColumnIndex(DbSchema.ProductTable.Cols.THUMBNAIL);
//        int nameIndex = cursor.getColumnIndex(DbSchema.ProductTable.Cols.NAME);
//        int priceIndex = cursor.getColumnIndex(DbSchema.ProductTable.Cols.PRICE);
//        int quantityIndex = cursor.getColumnIndex(DbSchema.ProductTable.Cols.QUANTITY);
//
//        while (!cursor.isLast()) {
//            cursor.moveToNext();
//            int id = cursor.getInt(idIndex);
//            String thumbnail = cursor.getString(thumbnailIndex);
//            String name = cursor.getString(nameIndex);
//            int price = cursor.getInt(priceIndex);
//            int quantity = cursor.getInt(quantityIndex);
//
//            Product product = new Product(id, thumbnail, name, price);
//            product.setQuantity(quantity);
//            products.add(product);
//        }
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
        statement.bindString(5, product.getQuantity() + "");

        long id = statement.executeInsert();

        if (id > 0) {
            //product.setId((int) id);
            return true;
        }

        return false;
    }

    public boolean delete(long id) {
        int result = db.delete(DbSchema.ProductTable.NAME, "id = ?", new String[]{id + ""});
        return result > 0;
    }

    public boolean update(Product product) {
        SQLiteStatement statement = db.compileStatement(UPDATE_STMT);
        statement.bindString(1, product.getThumbnail());
        statement.bindString(2, product.getName());
        statement.bindString(3, product.getUnitPrice() + "");
        statement.bindString(4, product.getQuantity() + "");
        statement.bindString(5, product.getId() + "");

        long result = statement.executeUpdateDelete();
        return result > 0;
    }

    public void clear() {
        db.execSQL(DELETE_ALL);
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
}
