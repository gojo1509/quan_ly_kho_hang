
package com.example.quan_ly_kho_hang;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quan_ly_kho_hang.database.DatabaseHelper;
import com.example.quan_ly_kho_hang.model.Product;

import java.text.NumberFormat;
import java.util.Locale;

public class ProductDetailActivity extends AppCompatActivity {

    private TextView txtName;
    private TextView txtCategory;
    private TextView txtPrice;
    private TextView txtQuantity;
    private TextView txtValue;
    private TextView txtStatus;
    private TextView txtDescription;
    private TextView txtWarning;

    private DatabaseHelper db;
    private int productId;
    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        txtName = findViewById(R.id.txtDetailName);
        txtCategory = findViewById(R.id.txtDetailCategory);
        txtPrice = findViewById(R.id.txtDetailPrice);
        txtQuantity = findViewById(R.id.txtDetailQuantity);
        txtValue = findViewById(R.id.txtDetailValue);
        txtStatus = findViewById(R.id.txtDetailStatus);
        txtDescription = findViewById(R.id.txtDetailDescription);
        txtWarning = findViewById(R.id.txtDetailWarning);

        Button btnImport = findViewById(R.id.btnImport);
        Button btnSell = findViewById(R.id.btnSell);
        Button btnBack = findViewById(R.id.btnBack);
        Button btnDelete = findViewById(R.id.btnDelete);

        db = new DatabaseHelper(this);
        productId = getIntent().getIntExtra("product_id", -1);

        btnImport.setOnClickListener(v -> updateStock(1));
        btnSell.setOnClickListener(v -> updateStock(-1));
        btnBack.setOnClickListener(v -> finish());
        btnDelete.setOnClickListener(v -> confirmDelete());
    }

    private void loadProduct() {
        product = db.getProductById(productId);

        if (product == null) {
            Toast.makeText(this, "Không tìm thấy sản phẩm", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));

        long totalValue = (long) product.getPrice() * product.getQuantity();

        txtName.setText(product.getName());
        txtCategory.setText("Loại sản phẩm: " + product.getCategory());
        txtPrice.setText("Giá bán: " + formatter.format(product.getPrice()) + " đ");
        txtQuantity.setText("Số lượng tồn: " + product.getQuantity());
        txtValue.setText("Giá trị tồn kho: " + formatter.format(totalValue) + " đ");
        txtDescription.setText("Mô tả: " + product.getDescription());
        txtStatus.setText(product.getActive() == 1 ? "Trạng thái: Đang kinh doanh" : "Trạng thái: Ngừng kinh doanh");

        if (product.getQuantity() < 5) {
            txtWarning.setVisibility(TextView.VISIBLE);
            txtQuantity.setTextColor(0xFFFF0000);
        } else {
            txtWarning.setVisibility(TextView.GONE);
            txtQuantity.setTextColor(0xFF000000);
        }
    }

    private void updateStock(int change) {
        if (product == null) {
            return;
        }

        int newQuantity = product.getQuantity() + change;

        if (newQuantity < 0) {
            Toast.makeText(this, "Hết hàng", Toast.LENGTH_SHORT).show();
            return;
        }

        db.updateQuantity(productId, newQuantity);
        loadProduct();

        if (change > 0) {
            Toast.makeText(this, "Đã nhập thêm 1 sản phẩm", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Đã bán 1 sản phẩm", Toast.LENGTH_SHORT).show();
        }
    }

    private void confirmDelete() {
        new AlertDialog.Builder(this)
                .setTitle("Xóa sản phẩm")
                .setMessage("Bạn có chắc chắn muốn xóa sản phẩm này?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    db.deleteProduct(productId);
                    Toast.makeText(this, "Đã xóa sản phẩm", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (db != null && productId != -1) {
            loadProduct();
        }
    }
}
