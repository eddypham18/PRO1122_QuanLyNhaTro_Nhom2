package group1.pro1122.duan1.daos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import group1.pro1122.duan1.database.DBhelper;
import group1.pro1122.duan1.models.ThongBao;

public class ThongBaoDAO {
    private final DBhelper dBhelper;

    public ThongBaoDAO(Context context){
        this.dBhelper = new DBhelper(context);
    }

    // Phương thức gửi thông báo
    public boolean sendThongBao(int userID, int hopDongID, int loaiThongBao, String noiDung) {
        SQLiteDatabase db = dBhelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("User_ID", userID);
        values.put("HopDong_ID", hopDongID);
        values.put("LoaiThongBao", loaiThongBao);
        values.put("NoiDung", noiDung);
        values.put("NgayGuiThongBao", String.valueOf(System.currentTimeMillis()));
        values.put("TrangThai", 0);

        try {
            long result = db.insert("ThongBao", null, values);
            db.close();
            return result != -1;  // Trả về true nếu insert thành công
        } catch (SQLiteException e) {
            Log.e("ThongBaoDAO", "Error inserting data: " + e.getMessage());
            db.close();
            return false;  // Nếu có lỗi trong quá trình insert
        }
    }


    public List<ThongBao> getAllThongBao(int userID) {
        List<ThongBao> thongBaoList = new ArrayList<>();
        SQLiteDatabase db = dBhelper.getReadableDatabase();

        String query = "SELECT * FROM ThongBao WHERE User_ID = ? ORDER BY NgayGuiThongBao DESC";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userID)});

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int thongBaoId = cursor.getInt(cursor.getColumnIndexOrThrow("ThongBao_ID"));
                int userId = cursor.getInt(cursor.getColumnIndexOrThrow("User_ID"));
                int hopDongId = cursor.getInt(cursor.getColumnIndexOrThrow("HopDong_ID"));
                int loaiThongBao = cursor.getInt(cursor.getColumnIndexOrThrow("LoaiThongBao"));
                String noiDung = cursor.getString(cursor.getColumnIndexOrThrow("NoiDung"));
                String ngayGuiThongBao = cursor.getString(cursor.getColumnIndexOrThrow("NgayGuiThongBao"));

                ThongBao thongBao = new ThongBao(thongBaoId, userId, hopDongId, loaiThongBao, noiDung, ngayGuiThongBao);
                thongBaoList.add(thongBao);
            }
            cursor.close();
        }
        db.close();
        return thongBaoList;
    }

    public int getUnreadNotificationCount(int userID) {
        SQLiteDatabase db = dBhelper.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM ThongBao WHERE TrangThai = 0 AND User_ID = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userID)});
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }

        cursor.close();
        db.close();
        return count;
    }

    public void markAllNotificationsAsRead(int userID) {
        SQLiteDatabase db = dBhelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("TrangThai", 1);

        db.update("ThongBao", values, "User_ID = ? AND TrangThai = 0", new String[]{String.valueOf(userID)});
        db.close();
    }

    public boolean deleteThongBao(int thongBaoId) {
        SQLiteDatabase db = dBhelper.getWritableDatabase();
        int rowsDeleted = db.delete("ThongBao", "ThongBao_ID = ?", new String[]{String.valueOf(thongBaoId)});
        db.close();
        return rowsDeleted > 0;
    }

}
