
package com.example.quan_ly_kho_hang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.quan_ly_kho_hang.R;
import com.example.quan_ly_kho_hang.model.Product;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ProductAdapter extends BaseAdapter {

    private final Context context;
    private final ArrayList<Product> products;

    public ProductAdapter(Context context, ArrayList<Product> products) {
        this.context = context;
        this.products = products;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int position) {
        return products.get(position);
    }

    @Override
    public long getItemId(int position) {
        return products.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.item_product,
                    parent,
                    false
            );
        }

        TextView txtName = convertView.findViewById(R.id.txtProductName);
        TextView txtPrice = convertView.findViewById(R.id.txtProductPrice);
        TextView txtCategory = convertView.findViewById(R.id.txtProductCategory);
        TextView txtQuantity = convertView.findViewById(R.id.txtProductQuantity);
        TextView txtLowStock = convertView.findViewById(R.id.txtLowStock);

        Product p = products.get(position);

        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));

        txtName.setText(p.getName());
        txtPrice.setText("Giá bán: " + formatter.format(p.getPrice()) + " đ");
        txtCategory.setText("Loại: " + p.getCategory());
        txtQuantity.setText("Số lượng: " + p.getQuantity());

        if (p.getQuantity() < 5) {
            txtLowStock.setVisibility(View.VISIBLE);
            txtQuantity.setTextColor(0xFFFF0000);
        } else {
            txtLowStock.setVisibility(View.GONE);
            txtQuantity.setTextColor(0xFF000000);
        }

        return convertView;
    }
}
