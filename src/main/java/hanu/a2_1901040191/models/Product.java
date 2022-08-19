package hanu.a2_1901040191.models;

import java.io.Serializable;

public class Product implements Serializable {
    private int id;
    private String thumbnail;
    private String name;
    private int unitPrice;
    private int quantity;

    public Product(int id, String thumbnail, String name, int unitPrice) {
        this.id = id;
        this.thumbnail = thumbnail;
        this.name = name;
        this.unitPrice = unitPrice;
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
     * @effects return thumbnail
     */
    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    /**
     * @effects return name
     */
    public String getName() {
        return name;
    }

    /**
     * @effects return name of the product that trimmed up to 41 characters
     */
    public String getTrimName() {
        if (name.length() > 38) {
            return name.substring(0, 38) + "...";
        } else {
            return name;
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * @effects return unitPrice
     */
    public int getUnitPrice() {
        return unitPrice;
    }

    /**
     * @effects return Formatted unitPrice in VND
     */
    public String getFormattedUnitPrice() {
        return "đ " + unitPrice;
    }

    public void setUnitPrice(int unitPrice) {
        this.unitPrice = unitPrice;
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
        return "Product{" +
                "id=" + id +
                ", thumbnail='" + thumbnail + '\'' +
                ", name='" + name + '\'' +
                ", unitPrice=" + unitPrice +
                ", quantity=" + quantity +
                '}';
    }

    public String toString(boolean excludeQuantity) {
        if (excludeQuantity) {
            return "Product{" +
                    "id=" + id +
                    ", thumbnail='" + thumbnail + '\'' +
                    ", name='" + name + '\'' +
                    ", unitPrice=" + unitPrice +
                    '}';
        } else {
            return toString();
        }
    }
}
