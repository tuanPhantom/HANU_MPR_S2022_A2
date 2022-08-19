package hanu.a2_1901040191.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "a2_1901040191.db";
    private static final int DB_VERSION = 1;

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create tables
        db.execSQL("CREATE TABLE " + DbSchema.ProductTable.NAME + " (" +
                DbSchema.ProductTable.Cols.ID + " INTEGER PRIMARY KEY, " +
                DbSchema.ProductTable.Cols.THUMBNAIL + " TEXT NOT NULL, " +
                DbSchema.ProductTable.Cols.NAME + " TEXT NOT NULL, " +
                DbSchema.ProductTable.Cols.PRICE + " INTEGER NOT NULL, " +
                DbSchema.ProductTable.Cols.QUANTITY + " INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // drop tables - warning: lost data
        db.execSQL("DROP TABLE IF EXISTS "+ DbSchema.ProductTable.NAME);

        // create again
        onCreate(db);
    }
}
