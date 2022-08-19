package hanu.a2_1901040191;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.HandlerCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import hanu.a2_1901040191.adapters.ProductAdapter;
import hanu.a2_1901040191.db.ProductManager;
import hanu.a2_1901040191.models.Constants;
import hanu.a2_1901040191.models.Product;

public class MainActivity extends AppCompatActivity {
    public static final int PRODUCT_ADDED = 1;

    private final Handler handler = HandlerCompat.createAsync(Looper.getMainLooper());
    private ProductManager productManager;

    private ProductAdapter adapter;
    private RecyclerView rwProducts;

    // init dataset
    private List<Product> products = new ArrayList<>();
    private List<Product> dbProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Constants.executor.execute(new Runnable() {
            @Override
            public void run() {
                String json = LoadJSON(Constants.APILink);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (json == null) {
                            Toast.makeText(MainActivity.this, "Oops, failed to connect!", Toast.LENGTH_SHORT).show();
                        } else {
                            try {
                                JSONArray root = new JSONArray(json);
                                for (int i = 0; i < root.length(); i++) {
                                    JSONObject jsonObject = (JSONObject) root.get(i);
                                    int id = jsonObject.getInt("id");
                                    String thumbnail = jsonObject.getString("thumbnail");
                                    String name = jsonObject.getString("name");
                                    int unitPrice = jsonObject.getInt("unitPrice");
                                    Product product = new Product(id, thumbnail, name, unitPrice);
                                    products.add(product);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        productManager = ProductManager.getInstance(MainActivity.this);
                        dbProducts = productManager.all();
                        if (!isPersistence()) {
                            rebuildDb();
                            dbProducts = productManager.all();
                            Log.i("AAAtuan", dbProducts + "");
                        }
                        Log.i("AAAtuan", isPersistence() + "");
                        products.clear();
                        products.addAll(dbProducts);
                        rwProducts = findViewById(R.id.rwProducts);
                        // set layout
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 2);
                        rwProducts.setLayoutManager(gridLayoutManager);
                        // init adapter
                        adapter = new ProductAdapter(products);
                        // bind RecycleView with adapter
                        rwProducts.setAdapter(adapter);
                    }
                });
            }
        });

        TextView searchbarTextView = findViewById(R.id.searchbarTextView);
        ImageView searchBtn = findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Product> match = search(searchbarTextView.getText().toString());
                RecyclerView rwProducts = findViewById(R.id.rwProducts);

                // refresh recycle view
                adapter = new ProductAdapter(match);
                rwProducts.setAdapter(adapter);
            }
        });

        ImageButton goToCart = findViewById(R.id.s1_go_to_cart);
        goToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CartActivity.class);
//                intent.putExtra("products", (Serializable) products);
//                Bundle extras = intent.getExtras();
//                List<Product> list = (List<Product>) extras.get("products");
//                Log.i("KKKtuan", "ket qua: "+(list==products));
                startActivity(intent);
            }
        });
    }

    /**
     * @Requires products!=null /\ dbProducts!=null
     * @effects <pre>
     *  if items in same index of products and dbProducts have different attribute values, but exclude the `quantity`
     *      return false
     *  else
     *      return true
     * </pre>
     */
    private boolean isPersistence() {
        if (products.size() != dbProducts.size()) {
            return false;
        }
        for (int i = 0; i < products.size(); i++) {
            Product p1 = products.get(i);
            Product p2 = dbProducts.get(i);
            if (!p1.toString(true).equals(p2.toString(true))) {
                return false;
            }
        }
        return true;
    }

    private boolean rebuildDb() {
        productManager.clear();
        for (Product product : products) {
            boolean result = productManager.add(product);
            if (!result) {
                return false;
            }
        }
        return true;
    }

    private String LoadJSON(String link) {
        URL url;
        HttpURLConnection urlConnection = null;
        InputStream is = null;
        try {
            url = new URL(link);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            is = urlConnection.getInputStream();
            Scanner sc = new Scanner(is);
            StringBuilder result = new StringBuilder();
            String line;
            while (sc.hasNextLine()) {
                line = sc.nextLine();
                result.append(line);
            }
            return result.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // close resources
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private List<Product> search(String content) {
        List<Product> match = new ArrayList<>();
        for (Product product : products) {
            if (product.getName().toLowerCase().contains(content.toLowerCase())) {
                match.add(product);
            }
        }
        return match;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PRODUCT_ADDED) {
            products.clear();
            products.addAll(productManager.all());
            adapter.notifyDataSetChanged();
        }
    }
}