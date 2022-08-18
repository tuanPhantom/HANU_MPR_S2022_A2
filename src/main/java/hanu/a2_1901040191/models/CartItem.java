package hanu.a2_1901040191.models;

import java.util.Collection;

public class CartItem {
    private int id;
    private int productId;
    private int quantity;

    public CartItem(int id, int productId, int quantity) {
        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
    }

    public CartItem(int productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    /**
     * @effects return id
     */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * @effects return productId
     */
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    /**
     * @effects return quantity
     */
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "id=" + id +
                ", productId=" + productId +
                ", quantity=" + quantity +
                '}';
    }

    public static CartItem findByProductId(int productId, Collection<CartItem> cartItems) {
        for (CartItem cartItem : cartItems) {
            if (cartItem.productId == productId) {
                return cartItem;
            }
        }
        return null;
    }
}
