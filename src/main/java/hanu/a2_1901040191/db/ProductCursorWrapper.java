package hanu.a2_1901040191.db;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.ArrayList;
import java.util.List;

import hanu.a2_1901040191.models.Product;

public class ProductCursorWrapper extends CursorWrapper {
    public ProductCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Product getProduct() {
        int idIndex = getColumnIndex(DbSchema.ProductTable.Cols.ID);
        int thumbnailIndex = getColumnIndex(DbSchema.ProductTable.Cols.THUMBNAIL);
        int nameIndex = getColumnIndex(DbSchema.ProductTable.Cols.NAME);
        int priceIndex = getColumnIndex(DbSchema.ProductTable.Cols.PRICE);
        int quantityIndex = getColumnIndex(DbSchema.ProductTable.Cols.QUANTITY);

        int id = getInt(idIndex);
        String thumbnail = getString(thumbnailIndex);
        String name = getString(nameIndex);
        int price = getInt(priceIndex);
        int quantity = getInt(quantityIndex);

        Product product = new Product(id, thumbnail, name, price);
        product.setQuantity(quantity);
        return product;
    }

    public List<Product> getProducts() {
        List<Product> products = new ArrayList<>();
        moveToFirst();
        while (!isAfterLast()) {
            Product product = getProduct();
            products.add(product);
            moveToNext();
        }
        return products;
    }
}
