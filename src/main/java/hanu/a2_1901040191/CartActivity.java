package hanu.a2_1901040191;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import hanu.a2_1901040191.adapters.CartProductAdapter;
import hanu.a2_1901040191.db.ProductManager;
import hanu.a2_1901040191.models.Product;

public class CartActivity extends AppCompatActivity {
    private CartProductAdapter adapter;
    private RecyclerView rwProducts;

    private List<Product> productsInCart = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_activity);

        ProductManager productManager = ProductManager.getInstance();
        List<Product> products = productManager.all();
        for (Product m : products) {
            if (m.getQuantity() > 0) {
                productsInCart.add(m);
            }
        }

        TextView footerTotalPriceTextView = findViewById(R.id.footer_price);

        rwProducts = findViewById(R.id.s2_rw);
        // set layout
        rwProducts.setLayoutManager(new LinearLayoutManager(CartActivity.this));
        // init adapter
        adapter = new CartProductAdapter(productsInCart, footerTotalPriceTextView);
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
