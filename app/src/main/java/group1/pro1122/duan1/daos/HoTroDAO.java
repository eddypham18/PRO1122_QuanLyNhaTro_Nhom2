package group1.pro1122.duan1.daos;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import group1.pro1122.duan1.database.DBhelper;
import group1.pro1122.duan1.models.HoTro;

public class HoTroDAO {
    private final DBhelper dBhelper;

    public HoTroDAO(Context context) {
        dBhelper = new DBhelper(context);
    }

    // Lấy tất cả các yêu cầu hỗ trợ
    public ArrayList<HoTro> getAllHoTroRequests() {
        SQLiteDatabase db = dBhelper.getReadableDatabase();
        ArrayList<HoTro> hoTroList = new ArrayList<>();
        String query = "SELECT * FROM HoTro";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int hoTroId = cursor.getInt(cursor.getColumnIndexOrThrow("HoTro_ID"));
                int userId = cursor.getInt(cursor.getColumnIndexOrThrow("User_ID"));
                String tieuDe = cursor.getString(cursor.getColumnIndexOrThrow("TieuDe"));
                String noiDung = cursor.getString(cursor.getColumnIndexOrThrow("NoiDung"));
                int trangThai = cursor.getInt(cursor.getColumnIndexOrThrow("TrangThai"));
                String ngayTao = cursor.getString(cursor.getColumnIndexOrThrow("NgayTao"));
                String ngayXuLy = cursor.getString(cursor.getColumnIndexOrThrow("NgayXuLy"));

                HoTro hoTro = new HoTro(hoTroId, userId, tieuDe, noiDung, trangThai, ngayTao, ngayXuLy);
                hoTroList.add(hoTro);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return hoTroList;
    }

    // Xóa yêu cầu hỗ trợ theo ID
    public boolean deleteHoTroRequest(int hoTroId) {
        SQLiteDatabase db = dBhelper.getWritableDatabase();
        return db.delete("HoTro", "HoTro_ID=?", new String[]{String.valueOf(hoTroId)}) > 0;
    }

    // Cập nhật trạng thái xử lý của yêu cầu hỗ trợ
    public boolean updateHoTroStatus(int hoTroId, int trangThai, String ngayXuLy) {
        SQLiteDatabase db = dBhelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("TrangThai", trangThai);
        values.put("NgayXuLy", ngayXuLy);

        int rowsAffected = db.update("HoTro", values, "HoTro_ID=?", new String[]{String.valueOf(hoTroId)});
        return rowsAffected > 0;
    }

    public boolean addHoTroRequest(HoTro hoTro) {
        SQLiteDatabase db = dBhelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("User_ID", hoTro.getUserId());
        values.put("TieuDe", hoTro.getTieuDe());
        values.put("NoiDung", hoTro.getNoiDung());
        values.put("TrangThai", hoTro.getTrangThai());
        values.put("NgayTao", hoTro.getNgayTao());
        values.put("NgayXuLy", hoTro.getNgayXuLy());

        long result = db.insert("HoTro", null, values);
        db.close();

        return result != -1;
    }


}
