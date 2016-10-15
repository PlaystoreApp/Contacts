package my.contacts2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by user-1 on 30-Jun-16.
 */
public class MyDB {


    public static final String DB_NAME = "mydb.db";
    public static final String TABLE_NAME = "students_tbl";
    public static final String STUDENT_NAME = "student_name";
    public static final String STUDENT_ROLL = "student_roll_num";
    public static final String STUDENT_ID = "_id";

    //public static final String CREATE_TBL = "CREATE TABLE students_tbl (_id INTEGER PRIMARY KEY AUTOINCREMENT, student_name TEXT, student_roll_num INTEGER)";
    public static final String CREATE_TBL = "CREATE TABLE "+TABLE_NAME
            +" ("+STUDENT_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            +STUDENT_NAME+" TEXT, "
            +STUDENT_ROLL+" INTEGER)";
    public static final String DROP_TBL = "DROP TABLE IF EXISTS "+TABLE_NAME;

    public static final int VER = 1;

    private SQLiteDatabase db;
    private MyDBOpenHelper myDBOpenHelper;

    public MyDB(Context context){
        myDBOpenHelper = new MyDBOpenHelper(context);
    }

    public void openDB() throws SQLiteException {
        db = myDBOpenHelper.getWritableDatabase();
    }

    public void closeDB(){
        db.close();
    }

    public long addUser(String name,int rollNum){
        ContentValues contentValues = new ContentValues();
        contentValues.put(STUDENT_NAME,name);
        contentValues.put(STUDENT_ROLL,rollNum);
        return db.insert(TABLE_NAME,null,contentValues);
    }

    public int updateUserById(long studId,String name,int rollNum){
        //UPDATE SET
        ContentValues contentValues = new ContentValues();
        contentValues.put(STUDENT_NAME,name);
        contentValues.put(STUDENT_ROLL,rollNum);
        return db.update(TABLE_NAME,contentValues,STUDENT_ID+ "= ?",new String[]{Long.toString(studId)});
    }

    public Cursor getAllUsers(){
        //SELECT * FROM students_tbl;
        Cursor cursor = db.query(TABLE_NAME,new String[]{STUDENT_ID,STUDENT_NAME,STUDENT_ROLL},null,null,null,null,null);
        cursor.moveToFirst();
        return cursor;
    }

    public Cursor getUserById(long stuId){
        //SELECT * FROM students_tbl WHERE _id = ?
        Cursor cursor = db.query(TABLE_NAME,new String[]{STUDENT_ID,STUDENT_NAME,STUDENT_ROLL},STUDENT_ID+" = ?",new String[]{Long.toString(stuId)},null,null,null);
        cursor.moveToFirst();
        return cursor;
    }

    public int deleteUser(long stuId){
        return db.delete(TABLE_NAME,STUDENT_ID+" = ?",new String[]{Long.toString(stuId)});
    }


    private class MyDBOpenHelper extends SQLiteOpenHelper {

        public MyDBOpenHelper(Context context) {
            super(context, DB_NAME, null, VER);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TBL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if (newVersion > oldVersion){
                db.execSQL(DROP_TBL);
                onCreate(db);
            }
        }
    }

}
