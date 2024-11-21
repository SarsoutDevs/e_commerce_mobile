package com.example.exercice.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SQLiteHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "products.db"
        private const val DATABASE_VERSION = 1

        const val TABLE_PRODUCTS = "products"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_PRICE = "price"
        const val COLUMN_IMAGE = "image"
        const val COLUMN_DESCRIPTION = "description"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_PRODUCTS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT NOT NULL,
                $COLUMN_PRICE REAL NOT NULL,
                $COLUMN_IMAGE INTEGER NOT NULL,
                $COLUMN_DESCRIPTION TEXT
            )
        """
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PRODUCTS")
        onCreate(db)
    }

    fun insertProduct(name: String, price: Double, image: Int, description: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_PRICE, price)
            put(COLUMN_IMAGE, image)
            put(COLUMN_DESCRIPTION, description)
        }
        return db.insert(TABLE_PRODUCTS, null, values)
    }

    fun getAllProducts(): List<Product> {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_PRODUCTS,
            null,
            null,
            null,
            null,
            null,
            null
        )

        val products = mutableListOf<Product>()
        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(COLUMN_ID))
                val name = getString(getColumnIndexOrThrow(COLUMN_NAME))
                val price = getDouble(getColumnIndexOrThrow(COLUMN_PRICE))
                val image = getInt(getColumnIndexOrThrow(COLUMN_IMAGE))
                val description = getString(getColumnIndexOrThrow(COLUMN_DESCRIPTION))

                products.add(Product(name, price, image, description))
            }
            close()
        }
        return products
    }

    fun updateProduct(id: Int, name: String, price: Double, image: Int, description: String): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_PRICE, price)
            put(COLUMN_IMAGE, image)
            put(COLUMN_DESCRIPTION, description)
        }
        return db.update(
            TABLE_PRODUCTS,
            values,
            "$COLUMN_ID = ?",
            arrayOf(id.toString())
        )
    }

    fun deleteProduct(id: Int): Int {
        val db = writableDatabase
        return db.delete(
            TABLE_PRODUCTS,
            "$COLUMN_ID = ?",
            arrayOf(id.toString())
        )
    }

    fun deleteAllProducts(): Int {
        val db = writableDatabase
        return db.delete(TABLE_PRODUCTS, null, null)
    }

}
