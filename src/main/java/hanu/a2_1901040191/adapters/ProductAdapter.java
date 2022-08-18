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
import hanu.a2_1901040191.MainActivity;
import hanu.a2_1901040191.R;
import hanu.a2_1901040191.db.EntitiesManager;
import hanu.a2_1901040191.models.CartItem;
import hanu.a2_1901040191.models.Product;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder> {
    // ViewHolder
    protected class ProductHolder extends RecyclerView.ViewHolder {
        public ProductHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bind(Product product) {
            //get ref to widgets
            TextView nameView = itemView.findViewById(R.id.name);

            ImageView imageView = itemView.findViewById(R.id.image);

            TextView priceView = itemView.findViewById(R.id.price);

            ImageButton imageButton = itemView.findViewById(R.id.imageButtonItem);

            // set data
            nameView.setText(product.getTrimName());

            ImageDownloader imageDownloader = new ImageDownloader(imageView);
            imageDownloader.execute(product.getThumbnail());

            priceView.setText(product.getFormattedUnitPrice());

            // handle events
            EntitiesManager entitiesManager = EntitiesManager.getInstance(itemView.getContext());
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<CartItem> cartItems = entitiesManager.getCartManager().all();
                    CartItem cartItem = CartItem.findByProductId(product.getId(), cartItems);
                    if (cartItem != null) {
                        cartItem.setQuantity(cartItem.getQuantity() + 1);
                        entitiesManager.getCartManager().update(cartItem);
                    } else {
                        entitiesManager.getCartManager().add(new CartItem(product.getId(),1));
                    }

                    Toast.makeText(itemView.getContext(), "added "+product.getTrimName(), Toast.LENGTH_SHORT).show();

                    imageButton.animate().scaleX(1.25f).scaleY(1.25f).setDuration(1000).withEndAction(
                            new Runnable() {
                                @Override
                                public void run() {
                                    imageButton.animate().scaleX(1f).scaleY(1f);
                                }
                            }
                    );
                }
            });
        }
    }

    // dataset
    private final List<Product> products;

    public ProductAdapter(List<Product> products) {
        this.products = products;
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
        View itemView = inflater.inflate(R.layout.item_product, parent, false);

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
