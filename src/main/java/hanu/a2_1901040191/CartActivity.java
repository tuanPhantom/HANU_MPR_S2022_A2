package hanu.a2_1901040191;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hanu.a2_1901040191.adapters.CartItemAdapter;
import hanu.a2_1901040191.db.EntitiesManager;
import hanu.a2_1901040191.models.CartItem;
import hanu.a2_1901040191.models.Product;

public class CartActivity extends AppCompatActivity {
    private CartItemAdapter adapter;
    private RecyclerView rwProducts;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_activity);

        EntitiesManager entitiesManager = EntitiesManager.getInstance(this);
        List<Product> products = entitiesManager.getProductManager().all();
        List<CartItem> cartItems = entitiesManager.getCartManager().all();

        TextView footerTotalPriceTextView = findViewById(R.id.footer_price);

        rwProducts = findViewById(R.id.s2_rw);
        // set layout
        rwProducts.setLayoutManager(new LinearLayoutManager(CartActivity.this));
        // init adapter
        adapter = new CartItemAdapter(cartItems, products, footerTotalPriceTextView);
        // bind RecycleView with adapter
        rwProducts.setAdapter(adapter);

        ImageButton backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
