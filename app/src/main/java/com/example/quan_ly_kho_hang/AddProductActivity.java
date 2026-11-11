
package com.example.quan_ly_kho_hang;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quan_ly_kho_hang.database.DatabaseHelper;
import com.example.quan_ly_kho_hang.model.Product;

public class AddProductActivity extends AppCompatActivity {

    private EditText edtProductName;
    private EditText edtProductPrice;
    private EditText edtProductQuantity;
    private EditText edtDescription;
    private Spinner spCategory;
    private Switch swActive;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        edtProductName = findViewById(R.id.edtProductName);
        edtProductPrice = findViewById(R.id.edtProductPrice);
        edtProductQuantity = findViewById(R.id.edtProductQuantity);
        edtDescription = findViewById(R.id.edtDescription);
        spCategory = findViewById(R.id.spCategory);
        swActive = findViewById(R.id.swActive);
        Button btnSaveProduct = findViewById(R.id.btnSaveProduct);

        db = new DatabaseHelper(this);

        String[] categories = {
                "Điện tử",
                "Thực phẩm",
                "Văn phòng phẩm",
                "Gia dụng",
                "Khác"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                categories
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(adapter);

        btnSaveProduct.setOnClickListener(v -> saveProduct());
    }

    private void saveProduct() {
        String name = edtProductName.getText().toString().trim();
        String priceText = edtProductPrice.getText().toString().trim();
        String quantityText = edtProductQuantity.getText().toString().trim();
        String category = spCategory.getSelectedItem().toString();
        String description = edtDescription.getText().toString().trim();

        if (name.isEmpty()) {
            edtProductName.setError("Vui lòng nhập tên sản phẩm");
            return;
        }

        if (priceText.isEmpty()) {
            edtProductPrice.setError("Vui lòng nhập giá bán");
            return;
        }

        if (quantityText.isEmpty()) {
            edtProductQuantity.setError("Vui lòng nhập số lượng");
            return;
        }

        int price;
        int quantity;

        try {
            price = Integer.parseInt(priceText);
            quantity = Integer.parseInt(quantityText);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Dữ liệu số không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        if (price <= 0) {
            edtProductPrice.setError("Giá bán phải lớn hơn 0");
            return;
        }

        if (quantity < 0) {
            edtProductQuantity.setError("Số lượng không được âm");
            return;
        }

        int active = swActive.isChecked() ? 1 : 0;

        Product product = new Product(
                name,
                price,
                quantity,
                category,
                active,
                description
        );

        long result = db.insertProduct(product);

        if (result != -1) {
            Toast.makeText(this, "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Không thể thêm sản phẩm", Toast.LENGTH_SHORT).show();
        }
    }
}
