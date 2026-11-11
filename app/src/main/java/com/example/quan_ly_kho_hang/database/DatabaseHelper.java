
package com.example.quan_ly_kho_hang.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.quan_ly_kho_hang.model.Product;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "warehouse.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_USERS = "users";
    private static final String TABLE_PRODUCTS = "products";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUsers = "CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT NOT NULL UNIQUE, " +
                "password TEXT NOT NULL)";

        String createProducts = "CREATE TABLE products (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "price INTEGER NOT NULL CHECK(price > 0), " +
                "quantity INTEGER NOT NULL CHECK(quantity >= 0), " +
                "category TEXT NOT NULL, " +
                "active INTEGER NOT NULL, " +
                "description TEXT)";

        db.execSQL(createUsers);
        db.execSQL(createProducts);

        ContentValues user = new ContentValues();
        user.put("username", "admin");
        user.put("password", "123456");
        db.insert(TABLE_USERS, null, user);

        insertSampleProduct(db, "Chuột không dây", 150000, 20, "Điện tử", 1, "Chuột không dây");
        insertSampleProduct(db, "Bàn phím cơ", 450000, 3, "Điện tử", 1, "Bàn phím cơ");
        insertSampleProduct(db, "Sổ tay A5", 25000, 40, "Văn phòng phẩm", 1, "Sổ tay A5");
    }

    private void insertSampleProduct(SQLiteDatabase db, String name, int price, int quantity, String category, int active, String description) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("price", price);
        values.put("quantity", quantity);
        values.put("category", category);
        values.put("active", active);
        values.put("description", description);
        db.insert(TABLE_PRODUCTS, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public boolean checkLogin(String username, String password) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT id FROM users WHERE username = ? AND password = ?",
                new String[]{username, password}
        );

        boolean result = cursor.moveToFirst();
        cursor.close();

        return result;
    }

    public long insertUser(String username, String password) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);

        return db.insert(TABLE_USERS, null, values);
    }

    public long insertProduct(Product p) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", p.getName());
        values.put("price", p.getPrice());
        values.put("quantity", p.getQuantity());
        values.put("category", p.getCategory());
        values.put("active", p.getActive());
        values.put("description", p.getDescription());

        return db.insert(TABLE_PRODUCTS, null, values);
    }

    public ArrayList<Product> getAllProducts() {
        ArrayList<Product> products = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM products ORDER BY id DESC", null);

        while (cursor.moveToNext()) {
            products.add(cursorToProduct(cursor));
        }

        cursor.close();

        return products;
    }

    public Product getProductById(int id) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM products WHERE id = ?",
                new String[]{String.valueOf(id)}
        );

        Product product = null;

        if (cursor.moveToFirst()) {
            product = cursorToProduct(cursor);
        }

        cursor.close();

        return product;
    }

    private Product cursorToProduct(Cursor cursor) {
        return new Product(
                cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                cursor.getString(cursor.getColumnIndexOrThrow("name")),
                cursor.getInt(cursor.getColumnIndexOrThrow("price")),
                cursor.getInt(cursor.getColumnIndexOrThrow("quantity")),
                cursor.getString(cursor.getColumnIndexOrThrow("category")),
                cursor.getInt(cursor.getColumnIndexOrThrow("active")),
                cursor.getString(cursor.getColumnIndexOrThrow("description"))
        );
    }

    public int updateQuantity(int id, int quantity) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("quantity", quantity);

        return db.update(
                TABLE_PRODUCTS,
                values,
                "id = ?",
                new String[]{String.valueOf(id)}
        );
    }

    public int deleteProduct(int id) {
        SQLiteDatabase db = getWritableDatabase();

        return db.delete(
                TABLE_PRODUCTS,
                "id = ?",
                new String[]{String.valueOf(id)}
        );
    }

    public long getTotalProducts() {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM products", null);
        long result = 0;

        if (cursor.moveToFirst()) {
            result = cursor.getLong(0);
        }

        cursor.close();

        return result;
    }

    public long getTotalValue() {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT SUM(price * quantity) FROM products",
                null
        );

        long result = 0;

        if (cursor.moveToFirst() && !cursor.isNull(0)) {
            result = cursor.getLong(0);
        }

        cursor.close();

        return result;
    }

    public int countLowStock() {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM products WHERE quantity < 5",
                null
        );

        int result = 0;

        if (cursor.moveToFirst()) {
            result = cursor.getInt(0);
        }

        cursor.close();

        return result;
    }
}
