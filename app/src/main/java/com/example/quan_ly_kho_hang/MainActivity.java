
package com.example.quan_ly_kho_hang;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quan_ly_kho_hang.adapter.ProductAdapter;
import com.example.quan_ly_kho_hang.database.DatabaseHelper;
import com.example.quan_ly_kho_hang.model.Product;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView txtStatistics;
    private ListView lvProducts;
    private DatabaseHelper db;
    private ArrayList<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtStatistics = findViewById(R.id.txtStatistics);
        lvProducts = findViewById(R.id.lvProducts);
        Button btnAddProduct = findViewById(R.id.btnAddProduct);

        db = new DatabaseHelper(this);

        btnAddProduct.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddProductActivity.class);
            startActivity(intent);
        });

        lvProducts.setOnItemClickListener((parent, view, position, id) -> {
            Product product = products.get(position);

            Intent intent = new Intent(MainActivity.this, ProductDetailActivity.class);
            intent.putExtra("product_id", product.getId());
            startActivity(intent);
        });
    }

    private void loadProducts() {
        products = db.getAllProducts();

        ProductAdapter adapter = new ProductAdapter(this, products);
        lvProducts.setAdapter(adapter);
    }

    private void loadStatistics() {
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));

        long totalProducts = db.getTotalProducts();
        long totalValue = db.getTotalValue();
        int lowStock = db.countLowStock();

        String statistics = "Tổng số sản phẩm: " + totalProducts +
                "\nTổng giá trị kho: " + formatter.format(totalValue) + " đ" +
                "\nSản phẩm sắp hết hàng: " + lowStock;

        txtStatistics.setText(statistics);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (db != null) {
            loadProducts();
            loadStatistics();
        }
    }
}
