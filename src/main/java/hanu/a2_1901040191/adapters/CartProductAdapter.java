package hanu.a2_1901040191.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hanu.a2_1901040191.ImageDownloader;
import hanu.a2_1901040191.R;
import hanu.a2_1901040191.db.ProductManager;
import hanu.a2_1901040191.models.Product;

public class CartProductAdapter extends RecyclerView.Adapter<CartProductAdapter.ProductHolder> {
    // ViewHolder
    protected class ProductHolder extends RecyclerView.ViewHolder {
        public ProductHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bind(Product product) {
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
            totalPrice.setText("đ " + singleSum(product));
            quantity.setText(product.getQuantity() + "");

            // handle events
            ProductManager productManager = ProductManager.getInstance(); // guarantee NotNull because I initialized it in MainActivity
            upBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // handle product
                    product.setQuantity(product.getQuantity() + 1);
                    productManager.update(product);

                    //handle footer total price
                    cartTotalPrice += product.getUnitPrice();
                    updateFooterUI();
                    CartProductAdapter.this.notifyDataSetChanged();
                }
            });

            downBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int quantity = product.getQuantity();
                    if (quantity > 0) {
                        // handle product
                        product.setQuantity(quantity - 1);
                        productManager.update(product);
                        if (product.getQuantity() == 0) {
                            products.remove(product);
                        }

                        //handle footer total price
                        cartTotalPrice -= product.getUnitPrice();
                        updateFooterUI();
                        CartProductAdapter.this.notifyDataSetChanged();
                    }
                }
            });
        }
    }

    // dataset
    private final List<Product> products;
    // derived attribute
    private int cartTotalPrice;
    // for Dependency Injection
    private TextView footerTotalPriceTextView;

    private int singleSum(Product product) {
        return product.getQuantity() * product.getUnitPrice();
    }

    private int footerSum() {
        int sum = 0;
        for (Product p : products) {
            sum += singleSum(p);
        }
        return sum;
    }

    private void updateFooterUI() {
        footerTotalPriceTextView.setText("đ " + cartTotalPrice);
    }

    public CartProductAdapter(List<Product> products, TextView footerTotalPriceTextView) {
        this.products = products;
        this.footerTotalPriceTextView = footerTotalPriceTextView;
        cartTotalPrice = footerSum();
        updateFooterUI();
    }

    @Override
    public int getItemCount() {
        return products.size();
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
        Product product = products.get(position);

        // bind data to the view
        holder.bind(product);
    }
}
