package group1.pro1122.duan1.daos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import group1.pro1122.duan1.database.DBhelper;
import group1.pro1122.duan1.models.ThanhToan;
import group1.pro1122.duan1.models.User;

public class ThanhToanDAO {
    private final DBhelper dBhelper;

    public ThanhToanDAO(Context context) {
        dBhelper = new DBhelper(context);
    }

    public User getNguoiThueByHopDongID(int hopDongID) {
        SQLiteDatabase db = dBhelper.getReadableDatabase();
        User nguoiThue = null;

        String query = "SELECT u.* " +
                "FROM HopDong hd " +
                "JOIN User u ON hd.NguoiThue = u.User_ID " +
                "WHERE hd.HopDong_ID = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(hopDongID)});

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int userId = cursor.getInt(cursor.getColumnIndexOrThrow("User_ID"));
                String username = cursor.getString(cursor.getColumnIndexOrThrow("Username"));
                String password = cursor.getString(cursor.getColumnIndexOrThrow("Password"));
                int vaiTro = cursor.getInt(cursor.getColumnIndexOrThrow("VaiTro"));
                String hoTen = cursor.getString(cursor.getColumnIndexOrThrow("HoTen"));
                String ngaySinh = cursor.getString(cursor.getColumnIndexOrThrow("NgaySinh"));
                String email = cursor.getString(cursor.getColumnIndexOrThrow("Email"));
                String sdt = cursor.getString(cursor.getColumnIndexOrThrow("SDT"));
                int trangThai = cursor.getInt(cursor.getColumnIndexOrThrow("TrangThai"));
                String cccd = cursor.getString(cursor.getColumnIndexOrThrow("CCCD"));
                String ngayTao = cursor.getString(cursor.getColumnIndexOrThrow("NgayTao"));

                nguoiThue = new User(userId, username, password, vaiTro, hoTen, ngaySinh, email, sdt, trangThai, cccd, ngayTao);
            }
            cursor.close();
        }

        db.close();

        return nguoiThue;
    }

    public ArrayList<ThanhToan> getThanhToanByChuTro(int chuTroID) {
        SQLiteDatabase db = dBhelper.getReadableDatabase();
        ArrayList<ThanhToan> thanhToanList = new ArrayList<>();

        String query = "SELECT tt.* " +
                "FROM ThanhToan tt " +
                "JOIN HopDong hd ON tt.HopDong_ID = hd.HopDong_ID " +
                "WHERE hd.ChuTro = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(chuTroID)});

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int thanhToanId = cursor.getInt(cursor.getColumnIndexOrThrow("ThanhToan_ID"));
                int hopDongId = cursor.getInt(cursor.getColumnIndexOrThrow("HopDong_ID"));
                String ngayTao = cursor.getString(cursor.getColumnIndexOrThrow("NgayTao"));
                int soDienTieuThu = cursor.getInt(cursor.getColumnIndexOrThrow("soDienTieuThu"));
                int soNuocTieuThu = cursor.getInt(cursor.getColumnIndexOrThrow("soNuocTieuThu"));
                int tongTien = cursor.getInt(cursor.getColumnIndexOrThrow("TongTien"));
                int trangThaiThanhToan = cursor.getInt(cursor.getColumnIndexOrThrow("TrangThaiThanhToan"));

                ThanhToan thanhToan = new ThanhToan(thanhToanId, hopDongId, ngayTao, soDienTieuThu, soNuocTieuThu, tongTien, trangThaiThanhToan);
                thanhToanList.add(thanhToan);
            }
            cursor.close();
        }

        db.close();

        return thanhToanList;
    }

    public ArrayList<ThanhToan> getThanhToanByChuTroAndTrangThaiIsTrue(int chuTroID) {
        SQLiteDatabase db = dBhelper.getReadableDatabase();
        ArrayList<ThanhToan> thanhToanList = new ArrayList<>();

        String query = "SELECT tt.* " +
                "FROM ThanhToan tt " +
                "JOIN HopDong hd ON tt.HopDong_ID = hd.HopDong_ID " +
                "WHERE hd.ChuTro = ? AND tt.TrangThaiThanhToan = 1";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(chuTroID)});

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int thanhToanId = cursor.getInt(cursor.getColumnIndexOrThrow("ThanhToan_ID"));
                int hopDongId = cursor.getInt(cursor.getColumnIndexOrThrow("HopDong_ID"));
                String ngayTao = cursor.getString(cursor.getColumnIndexOrThrow("NgayTao"));
                int soDienTieuThu = cursor.getInt(cursor.getColumnIndexOrThrow("soDienTieuThu"));
                int soNuocTieuThu = cursor.getInt(cursor.getColumnIndexOrThrow("soNuocTieuThu"));
                int tongTien = cursor.getInt(cursor.getColumnIndexOrThrow("TongTien"));
                int trangThaiThanhToan = cursor.getInt(cursor.getColumnIndexOrThrow("TrangThaiThanhToan"));

                ThanhToan thanhToan = new ThanhToan(thanhToanId, hopDongId, ngayTao, soDienTieuThu, soNuocTieuThu, tongTien, trangThaiThanhToan);
                thanhToanList.add(thanhToan);
            }
            cursor.close();
        }

        db.close();

        return thanhToanList;
    }

    public ArrayList<ThanhToan> getThanhToanByNguoiThue(int nguoiThueID) {
        SQLiteDatabase db = dBhelper.getReadableDatabase();
        ArrayList<ThanhToan> thanhToanList = new ArrayList<>();

        String query = "SELECT tt.* " +
                "FROM ThanhToan tt " +
                "JOIN HopDong hd ON tt.HopDong_ID = hd.HopDong_ID " +
                "WHERE hd.NguoiThue = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(nguoiThueID)});

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int thanhToanId = cursor.getInt(cursor.getColumnIndexOrThrow("ThanhToan_ID"));
                int hopDongId = cursor.getInt(cursor.getColumnIndexOrThrow("HopDong_ID"));
                String ngayTao = cursor.getString(cursor.getColumnIndexOrThrow("NgayTao"));
                int soDienTieuThu = cursor.getInt(cursor.getColumnIndexOrThrow("soDienTieuThu"));
                int soNuocTieuThu = cursor.getInt(cursor.getColumnIndexOrThrow("soNuocTieuThu"));
                int tongTien = cursor.getInt(cursor.getColumnIndexOrThrow("TongTien"));
                int trangThaiThanhToan = cursor.getInt(cursor.getColumnIndexOrThrow("TrangThaiThanhToan"));

                ThanhToan thanhToan = new ThanhToan(thanhToanId, hopDongId, ngayTao, soDienTieuThu, soNuocTieuThu, tongTien, trangThaiThanhToan);
                thanhToanList.add(thanhToan);
            }
            cursor.close();
        }

        db.close();

        return thanhToanList;
    }

    public boolean addThanhToan(ThanhToan thanhToan) {
        SQLiteDatabase db = dBhelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("HopDong_ID", thanhToan.getHopDongId());
        values.put("NgayTao", thanhToan.getNgayTao());
        values.put("soDienTieuThu", thanhToan.getSoDien());
        values.put("soNuocTieuThu", thanhToan.getSoNuoc());
        values.put("TongTien", thanhToan.getTongTien());
        values.put("TrangThaiThanhToan", thanhToan.getTrangThaiThanhToan());

        long result = db.insert("ThanhToan", null, values);
        db.close();
        return result != -1;
    }

    public boolean updateTrangThaiThanhToan(int thanhToanId, int trangThaiMoi) {
        SQLiteDatabase db = dBhelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("TrangThaiThanhToan", trangThaiMoi);

        int rowsUpdated = db.update("ThanhToan", values, "ThanhToan_ID = ?", new String[]{String.valueOf(thanhToanId)});
        db.close();
        return rowsUpdated > 0;
    }


    public boolean deleteThanhToan(int thanhToanId) {
        SQLiteDatabase db = dBhelper.getWritableDatabase();

        // Xóa phiếu thanh toán dựa trên ID
        int rowsDeleted = db.delete("ThanhToan", "ThanhToan_ID = ?", new String[]{String.valueOf(thanhToanId)});
        db.close();
        return rowsDeleted > 0;
    }

}
