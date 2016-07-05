package johnbryce.com.chefbro;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.Context;
import android.content.ContentValues;

public class DBHandler extends SQLiteOpenHelper  {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "userdetails.db";
    private static final String TABLE_USERPIC = "userpicture";
    private static final String COLUMN_UID="uid";
    private static final String COLUMN_PROFILEPIC="picture";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        //super(context, name, factory, version);
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_USERPIC + "(" +
                COLUMN_UID+ " TEXT," +
                COLUMN_PROFILEPIC+ " BLOB" +
                ");";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIST " + TABLE_USERPIC);
        onCreate(db);
    }

    public void setUser(String userUID, byte[] profilePic)
    {
        // Delete and add the nre user
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_USERPIC );
        ContentValues values = new ContentValues();
        values.put(COLUMN_UID, userUID);
        values.put(COLUMN_PROFILEPIC, profilePic);
        db.insert(TABLE_USERPIC, null, values);
        db.close();
    }

    public byte[] getUserPicture(String userUID)
    {
        byte[] picture = null;
        SQLiteDatabase db = getWritableDatabase();

        String query = "SELECT * FROM "+ TABLE_USERPIC + " WHERE "+ COLUMN_UID + "=\""+ userUID +"\"";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        while(!c.isAfterLast()){
            if(c.getBlob(c.getColumnIndex(COLUMN_PROFILEPIC))!=null){
                picture = c.getBlob(c.getColumnIndex(COLUMN_PROFILEPIC));
            }
            c.moveToNext();
        }
        db.close();
        return picture;
    }

    public boolean isExist(String userUID)
    {
        boolean found = false;
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM "+ TABLE_USERPIC + " WHERE "+ COLUMN_UID + "=\""+ userUID +"\"";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        while(!c.isAfterLast()){
            if(c.getBlob(c.getColumnIndex(COLUMN_PROFILEPIC))!=null){
                found = true;
            }
            c.moveToNext();
        }
        db.close();
        return found;
    }


}
