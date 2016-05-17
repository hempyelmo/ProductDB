package asterion.com.productdb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.List;
import java.util.LinkedList;

/**
 * From http://hmkcode.com/android-simple-sqlite-database-tutorial/
 *
 *
 */

public class MySQLiteHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "ProductDB";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_PRODUCT_TABLE = "CREATE TABLE products (" +
                "id INT PRIMARY KEY," +
                "loc INT	NOT NULL," +
                "McKIdNb	CHAR(6)," +
                "UPC CHAR(12)	NOT NULL," +
                "description	TEXT," +
                "format        TEXT," +
                "nbFacing	INT NOT NULL," +
                "shelfNb	INT NOT NULL," +
                "shelfHeight INT NOT NULL)";

        db.execSQL(CREATE_PRODUCT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS products");
        this.onCreate(db);
    }

    private static final String TABLE_PRODUCTS = "products";

    private static final String KEY_ID = "id";
    private static final String KEY_LOC = "loc";
    private static final String KEY_IDNB = "McKIdNb";
    private static final String KEY_UPC = "UPC";
    private static final String KEY_DESC = "description";
    private static final String KEY_FORMAT = "format";
    private static final String KEY_NBFACING = "nbFacing";
    private static final String KEY_SHELFNB = "shelfNb";
    private static final String KEY_SHELFHGT = "shelfHeight";

    private static final String[] COLUMNS = {KEY_ID,KEY_LOC,KEY_IDNB,KEY_UPC,
            KEY_DESC,KEY_FORMAT,KEY_NBFACING,KEY_SHELFNB,KEY_SHELFHGT};

    private int mLoc = 1;

    public void addProduct(Product product){
        Log.d("addProduct", product.toString());
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_ID, mLoc);
        values.put(KEY_LOC, mLoc);
        values.put(KEY_IDNB, product.getIdNb());
        values.put(KEY_UPC, product.getUpc());
        values.put(KEY_DESC, product.getDesc());
        values.put(KEY_FORMAT, product.getFormat());
        values.put(KEY_NBFACING, product.getNbFacing());
        values.put(KEY_SHELFNB, product.getShelfNb());
        values.put(KEY_SHELFHGT, product.getShelfHeight());

        // 3. insert
        db.insert(TABLE_PRODUCTS, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();

        mLoc++;
    }

    public void insertProduct(Product product, int loc) {
        Log.d("insertProduct", product.toString());
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_ID, mLoc);
        values.put(KEY_LOC, loc);
        values.put(KEY_IDNB, product.getIdNb());
        values.put(KEY_UPC, product.getUpc());
        values.put(KEY_DESC, product.getDesc());
        values.put(KEY_FORMAT, product.getFormat());
        values.put(KEY_NBFACING, product.getNbFacing());
        values.put(KEY_SHELFNB, product.getShelfNb());
        values.put(KEY_SHELFHGT, product.getShelfHeight());

        String INCREMENT_LOC = "UPDATE " + TABLE_PRODUCTS + " SET " +
                KEY_LOC + " = " + KEY_LOC + " + 1 WHERE " + KEY_LOC + " >= " + loc;

        db.execSQL(INCREMENT_LOC);

        // 3. insert
        db.insert(TABLE_PRODUCTS, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();

        mLoc++;
    }

    public Product getProduct(int loc){

        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query(TABLE_PRODUCTS, // a. table
                        COLUMNS, // b. column names
                        " " + KEY_LOC + " = ?", // c. selections
                        new String[] { String.valueOf(loc) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        Product product = new Product();
        // 3. if we got results get the first one
        if (cursor != null) {
            cursor.moveToFirst();

            product.setIdNb(cursor.getString(2));
            product.setUpc(cursor.getString(3));
            product.setDesc(cursor.getString(4));
            product.setFormat(cursor.getString(5));
            product.setNbFacing(Integer.parseInt(cursor.getString(6)));
            product.setShelfNb(Integer.parseInt(cursor.getString(7)));
            product.setShelfHeight(Integer.parseInt(cursor.getString(8)));

            Log.d("getProduct(" + loc + ")", product.toString());

            cursor.close();
        } else {
            product = null;
        }

        return product;
    }

    public List<Product> getAllProducts() {
        List<Product> products = new LinkedList<>();

        // 1. build the query
        String query = "SELECT * FROM " + TABLE_PRODUCTS;

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        Product product;
        if (cursor.moveToFirst()) {
            do {
                product = new Product();
                product.setIdNb(cursor.getString(2));
                product.setUpc(cursor.getString(3));
                product.setDesc(cursor.getString(4));
                product.setFormat(cursor.getString(5));
                product.setNbFacing(Integer.parseInt(cursor.getString(6)));
                product.setShelfNb(Integer.parseInt(cursor.getString(7)));
                product.setShelfHeight(Integer.parseInt(cursor.getString(8)));

                products.add(product);
            } while (cursor.moveToNext());
        }

        Log.d("getAllProducts()", products.toString());

        cursor.close();

        return products;
    }

    public int updateProduct(Product product) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_ID,String.valueOf((Object) null));
        values.put(KEY_IDNB, product.getIdNb());
        values.put(KEY_UPC, product.getUpc());
        values.put(KEY_DESC, product.getDesc());
        values.put(KEY_FORMAT, product.getFormat());
        values.put(KEY_NBFACING, product.getNbFacing());
        values.put(KEY_SHELFNB, product.getShelfNb());
        values.put(KEY_SHELFHGT, product.getShelfHeight());

      /*  // 3. updating row
        int i = db.update(TABLE_PRODUCTS, //table
                values, // column/value
                KEY_LOC+" = ?", // selections
                new String[] { String.valueOf(product.getIdNb()) }); //selection args

        // 4. close
        db.close();*/

        int i = 0;
        return i;

    }

    public void deleteProduct(Product product) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. delete
       /* db.delete(TABLE_PRODUCTS,
                KEY_LOC+" = ?",
                new String[] { String.valueOf(product.getId()) });

        // 3. close*/
        db.close();

        Log.d("deleteProduct", product.toString());

    }

    public List<Product> findProduct(String column, String operator, String value) {

        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE "
                + column + " " + operator + " " + value;

        // 2. build query
        Cursor cursor = db.rawQuery(query, null);

        List<Product> products = new LinkedList<>();
        // 3. if we got results get the first one
        Product product;
        if (cursor.moveToFirst()) {
            do {
                product = new Product();
                product.setIdNb(cursor.getString(2));
                product.setUpc(cursor.getString(3));
                product.setDesc(cursor.getString(4));
                product.setFormat(cursor.getString(5));
                product.setNbFacing(Integer.parseInt(cursor.getString(6)));
                product.setShelfNb(Integer.parseInt(cursor.getString(7)));
                product.setShelfHeight(Integer.parseInt(cursor.getString(8)));

                products.add(product);
            } while (cursor.moveToNext());
        } else {
            products.add(null);
        }

        return products;
    }

    public int getNbProducts() {
        return mLoc - 1;
    }

    public int getLoc(int id) {
        // 1. get reference to readable DB
       SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query(TABLE_PRODUCTS, // a. table
                        new String[] {KEY_LOC}, // b. column names
                        " " + KEY_ID + " = ?", // c. selections
                        new String[] { String.valueOf(id) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. if we got results get the first one
        if (cursor != null) {
            cursor.moveToFirst();

            Log.d("getLoc",cursor.getString(0));
            return cursor.getInt(0);
        } else {
            return 0;
        }
    }

    public int getId(int loc) {
        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query(TABLE_PRODUCTS, // a. table
                        new String[] {KEY_ID}, // b. column names
                        " " + KEY_LOC + " = ?", // c. selections
                        new String[] { String.valueOf(loc) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. if we got results get the first one
        if (cursor != null) {
            cursor.moveToFirst();

            Log.d("getId",cursor.getString(0));
            return cursor.getInt(0);
        } else {
            return 0;
        }
    }

}
