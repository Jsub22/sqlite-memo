package com.example.sqliteexample;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper
{
    private static SQLiteDatabase mDB;
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "mydiarlist.db";

    public DBHelper(@Nullable Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        // 데이터 베이스가 생성될 때 호출
        // 데이터베이스 -> 테이블 -> 컬럼 -> 값
        String strQuery = "CREATE TABLE IF NOT EXISTS DiarList (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT NOT NULL, content TEXT NOT NULL, writeDate TEXT NOT NULL)";
        // 존재하면 추가하지 않고 존재하지 않으면 추가함, 주키 설정, 자동 증가,
        db.execSQL(strQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        onCreate(db);
    }

    // SELECT 문 (할 일 목록을 조회)
    public ArrayList<DiarItem> getDiarItem()
    {
        ArrayList<DiarItem> diarItems = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM DiarList ORDER BY writeDate DESC", null);
        if(cursor.getCount() != 0) {
            // 조회온 데이터가 있을 때 내부 수정
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String content = cursor.getString(cursor.getColumnIndex("content"));
                String writeDate = cursor.getString(cursor.getColumnIndex("writeDate"));

                DiarItem diarItem =  new DiarItem();
                diarItem.setTitle(title);
                diarItem.setContent(content);
                diarItem.setWriteDate(writeDate);
                diarItems.add(diarItem);
            }
        }
        cursor.close();
        return diarItems;
    }

    // INSERT 문 (할 일 목록을 DB에 넣음)

    public void InsertList(String _title, String _content, String _writeDate) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO DiarList(title, content, writeDate) VALUES('" + _title + "', '" + _content + "', '" + _writeDate + "')");
    }

    // UPDATE 문 (할 일 목록을 DB에서 수정함)
    public void UpdateList(String _beforeDate, String _title, String _content, String _writeDate) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE DiarList SET title='" + _title + "', content='" + _content + "', writeDate='" + _writeDate + "' WHERE writeDate='" + _beforeDate + "'");
    }

    // DELETE 문 (할 일 목록을 DB에서 제거함)
    public void DeleteList(String _beforeDate) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM DiarList WHERE writeDate='" + _beforeDate + "'");
    }
}
