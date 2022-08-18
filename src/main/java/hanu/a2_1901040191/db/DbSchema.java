package hanu.a2_1901040191.db;

public class DbSchema {
    public final class ProductTable {
        public static final String NAME = "products";

        public final class Cols {
            public static final String ID = "id";
            public static final String THUMBNAIL = "thumbnail";
            public static final String NAME = "name";
            public static final String PRICE = "unitPrice";
        }
    }

    public final class CartItemTable {
        public static final String NAME = "cartItems";

        public final class Cols {
            public static final String ID = "id";
            public static final String PRODUCT_ID = "productId";
            public static final String QUANTITY = "quantity";
        }
    }
}
