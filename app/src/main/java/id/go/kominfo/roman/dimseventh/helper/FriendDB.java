package id.go.kominfo.roman.dimseventh.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import id.go.kominfo.roman.dimseventh.dao.FriendDao;
import id.go.kominfo.roman.dimseventh.model.Friend;

public class FriendDB extends SQLiteOpenHelper implements FriendDao {
    public static final String DBNAME = "vsga.db";
    public static final int DBVERSION = 2;

    public FriendDB(@Nullable Context context) {
        super(context, DBNAME, null, DBVERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table if not exists friend(" +
                "id integer primary key autoincrement," +
                "name text)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            db.execSQL("drop table if exists friend");
            onCreate(db);
        }
    }

    @Override
    public long insert(Friend friend) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.putNull("id");
        values.put("name", friend.getName());
        return db.insert("friend", null, values);
    }

    @Override
    public void update(Friend friend) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", friend.getId());
        values.put("name", friend.getName());
        db.update("friend", values, "id=?", new String[]{String.valueOf(friend.getId())});
    }

    @Override
    public void delete(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("friend", "id=?", new String[]{String.valueOf(id)});
    }

    @Override
    public Friend getAFriendById(int id) {
        Friend result = null;
        SQLiteDatabase db = getReadableDatabase();
        try (Cursor c = db.query("friend", null, "id=?", null, null, null, null)) {
            if (c.moveToFirst())
                result = new Friend(c.getInt(0), c.getString(1));
        }
        return result;
    }

    @Override
    public List<Friend> getAllFriends() {
        List<Friend> result = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        try (Cursor c = db.query("friend", null, null, null, null, null, null)) {
            while (c.moveToNext())
                result.add(new Friend(c.getInt(0), c.getString(1)));
        }

        return result;
    }
}
