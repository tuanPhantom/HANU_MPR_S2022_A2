package hanu.a2_1901040191.db;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.ArrayList;
import java.util.List;

import hanu.a2_1901040191.models.CartItem;

public class CartItemCursorWrapper extends CursorWrapper {
    public CartItemCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public CartItem getCart() {
        int idIndex = getColumnIndex(DbSchema.CartItemTable.Cols.ID);
        int productIdIndex = getColumnIndex(DbSchema.CartItemTable.Cols.PRODUCT_ID);
        int quantityIndex = getColumnIndex(DbSchema.CartItemTable.Cols.QUANTITY);


        int id = getInt(idIndex);
        int productId = getInt(productIdIndex);
        int quantity = getInt(quantityIndex);

        CartItem cartItem = new CartItem(id, productId,quantity);
        return cartItem;
    }

    public List<CartItem> getCarts() {
        List<CartItem> cartItems = new ArrayList<>();
        moveToFirst();
        while (!isAfterLast()) {
            CartItem cartItem = getCart();
            cartItems.add(cartItem);
            moveToNext();
        }
        return cartItems;
    }
}
