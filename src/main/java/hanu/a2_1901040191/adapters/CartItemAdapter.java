package hanu.a2_1901040191.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hanu.a2_1901040191.ImageDownloader;
import hanu.a2_1901040191.R;
import hanu.a2_1901040191.db.EntitiesManager;
import hanu.a2_1901040191.models.CartItem;
import hanu.a2_1901040191.models.Product;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.ProductHolder> {
    // ViewHolder
    protected class ProductHolder extends RecyclerView.ViewHolder {
        public ProductHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bind(CartItem cartItem) {
            // get ref of corresponding product
            Product product = Product.findByCartItem(cartItem, products);

            //get ref to widgets
            ImageView imageView = itemView.findViewById(R.id.imageView);
            TextView nameView = itemView.findViewById(R.id.name2);
            TextView priceView = itemView.findViewById(R.id.single_price);
            ImageButton upBtn = itemView.findViewById(R.id.upBtn);
            ImageButton downBtn = itemView.findViewById(R.id.downBtn);
            TextView totalPrice = itemView.findViewById(R.id.total_price);
            TextView quantity = itemView.findViewById(R.id.quantity);

            // set data
            ImageDownloader imageDownloader = new ImageDownloader(imageView);
            imageDownloader.execute(product.getThumbnail());

            nameView.setText(product.getTrimName());
            priceView.setText(product.getFormattedUnitPrice());

            int sum = singleProductSum(product);

            totalPrice.setText("đ " + sum);
            quantity.setText(cartItem.getQuantity() + "");

            // handle events
            EntitiesManager entitiesManager = EntitiesManager.getInstance(itemView.getContext());
            Product finalProduct = product;
            upBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // handle cartItem
                    cartItem.setQuantity(cartItem.getQuantity() + 1);
                    entitiesManager.getCartManager().update(cartItem);

                    //handle footer total price
                    cartTotalPrice += finalProduct.getUnitPrice();
                    updateFooterUI();
                    CartItemAdapter.this.notifyDataSetChanged();
                }
            });

            downBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int quantity = cartItem.getQuantity();
                    if (quantity > 0) {
                        // handle cartItem
                        cartItem.setQuantity(quantity - 1);
                        entitiesManager.getCartManager().update(cartItem);
                        if (cartItem.getQuantity() == 0) {
                            entitiesManager.getCartManager().delete(cartItem.getId());
                            cartItems.remove(cartItem);
                            Toast.makeText(itemView.getContext(), "Removed "+product.getTrimName(), Toast.LENGTH_SHORT).show();
                        }

                        //handle footer total price
                        cartTotalPrice -= finalProduct.getUnitPrice();
                        updateFooterUI();
                        CartItemAdapter.this.notifyDataSetChanged();
                    }
                }
            });
        }
    }

    // dataset
    private final List<Product> products;
    private final List<CartItem> cartItems;
    // derived attribute
    private int cartTotalPrice;
    // for Dependency Injection
    private TextView cartTotalPriceTextView;

    private int singleProductSum(Product product) {
        return product.getUnitPrice() * CartItem.findByProductId(product.getId(), cartItems).getQuantity();
    }

    private int calculate_cartTotalPrice() {
        int sum = 0;
        for (CartItem cartItem : cartItems) {
            sum += Product.findByCartItem(cartItem, products).getUnitPrice() * cartItem.getQuantity();
        }
        return sum;
    }

    private void updateFooterUI() {
        cartTotalPriceTextView.setText("đ " + cartTotalPrice);
    }

    public CartItemAdapter(List<CartItem> cartItems, List<Product> products, TextView cartTotalPriceTextView) {
        this.cartItems = cartItems;
        this.products = products;
        this.cartTotalPriceTextView = cartTotalPriceTextView;
        cartTotalPrice = calculate_cartTotalPrice();
        updateFooterUI();
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //context
        Context context = parent.getContext();

        //layout inflater
        LayoutInflater inflater = LayoutInflater.from(context);

        // inflate - view
        View itemView = inflater.inflate(R.layout.item_cart, parent, false);

        return new ProductHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHolder holder, int position) {
        // get data item at pos
        CartItem cartItem = cartItems.get(position);

        // bind data to the view
        holder.bind(cartItem);
    }
}
