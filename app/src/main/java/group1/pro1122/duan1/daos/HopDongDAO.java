package group1.pro1122.duan1.daos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import group1.pro1122.duan1.database.DBhelper;
import group1.pro1122.duan1.models.HopDong;

public class HopDongDAO {
    private final DBhelper dBhelper;

    public HopDongDAO(Context context){
        dBhelper = new DBhelper(context);
    }


    public HopDong getHopDongByHopDongID(int hopDongID) {
        SQLiteDatabase db = dBhelper.getReadableDatabase();
        HopDong hopDong = null;

        String query = "SELECT * FROM HopDong WHERE HopDong_ID = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(hopDongID)});

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                // Lấy thông tin từ Cursor
                int phongID = cursor.getInt(cursor.getColumnIndexOrThrow("Phong_ID"));
                int nguoiThue = cursor.getInt(cursor.getColumnIndexOrThrow("NguoiThue"));
                int chuTro = cursor.getInt(cursor.getColumnIndexOrThrow("ChuTro"));
                int phiDichVuID = cursor.getInt(cursor.getColumnIndexOrThrow("PhiDichVu_ID"));
                String ngayTaoHopDong = cursor.getString(cursor.getColumnIndexOrThrow("NgayTaoHopDong"));
                String ngayKetThuc = cursor.getString(cursor.getColumnIndexOrThrow("NgayKetThuc"));
                int trangThaiHopDong = cursor.getInt(cursor.getColumnIndexOrThrow("TrangThaiHopDong"));

                // Tạo đối tượng HopDong từ dữ liệu
                hopDong = new HopDong(hopDongID, phongID, nguoiThue, chuTro, phiDichVuID, ngayTaoHopDong, ngayKetThuc, trangThaiHopDong);
            }
            cursor.close(); // Đóng Cursor
        }

        db.close(); // Đóng kết nối cơ sở dữ liệu
        return hopDong; // Trả về hợp đồng hoặc null nếu không tìm thấy
    }
    public ArrayList<HopDong> readByIDChuTro(int userID){
        ArrayList<HopDong> listHopDong = new ArrayList<>();
        SQLiteDatabase db = dBhelper.getReadableDatabase();

        String query = "SELECT * FROM HopDong WHERE ChuTro = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userID)});
        if(cursor!=null){
            while(cursor.moveToNext()){
                int hopDongID = cursor.getInt(cursor.getColumnIndexOrThrow("HopDong_ID"));
                int phongID = cursor.getInt(cursor.getColumnIndexOrThrow("Phong_ID"));
                int nguoiThue = cursor.getInt(cursor.getColumnIndexOrThrow("NguoiThue"));
                int chuTro = cursor.getInt(cursor.getColumnIndexOrThrow("ChuTro"));
                int phiDichVuID = cursor.getInt(cursor.getColumnIndexOrThrow("PhiDichVu_ID"));
                String ngayTaoHopDong = cursor.getString(cursor.getColumnIndexOrThrow("NgayTaoHopDong"));
                String ngayKetThuc = cursor.getString(cursor.getColumnIndexOrThrow("NgayKetThuc"));
                int trangThaiHopDong = cursor.getInt(cursor.getColumnIndexOrThrow("TrangThaiHopDong"));

                // Thêm đối tượng HopDong vào danh sách
                listHopDong.add(new HopDong(hopDongID, phongID, nguoiThue, chuTro, phiDichVuID, ngayTaoHopDong, ngayKetThuc, trangThaiHopDong));
            }
            cursor.close();
        }

        db.close();

        return listHopDong;
    }

    public ArrayList<HopDong> readByIDNguoiThue(int userID){
        ArrayList<HopDong> listHopDong = new ArrayList<>();
        SQLiteDatabase db = dBhelper.getReadableDatabase();

        String query = "SELECT * FROM HopDong WHERE NguoiThue = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userID)});
        if(cursor!=null){
            while(cursor.moveToNext()){
                int hopDongID = cursor.getInt(cursor.getColumnIndexOrThrow("HopDong_ID"));
                int phongID = cursor.getInt(cursor.getColumnIndexOrThrow("Phong_ID"));
                int nguoiThue = cursor.getInt(cursor.getColumnIndexOrThrow("NguoiThue"));
                int chuTro = cursor.getInt(cursor.getColumnIndexOrThrow("ChuTro"));
                int phiDichVuID = cursor.getInt(cursor.getColumnIndexOrThrow("PhiDichVu_ID"));
                String ngayTaoHopDong = cursor.getString(cursor.getColumnIndexOrThrow("NgayTaoHopDong"));
                String ngayKetThuc = cursor.getString(cursor.getColumnIndexOrThrow("NgayKetThuc"));
                int trangThaiHopDong = cursor.getInt(cursor.getColumnIndexOrThrow("TrangThaiHopDong"));

                // Thêm đối tượng HopDong vào danh sách
                listHopDong.add(new HopDong(hopDongID, phongID, nguoiThue, chuTro, phiDichVuID, ngayTaoHopDong, ngayKetThuc, trangThaiHopDong));
            }
            cursor.close();
        }

        db.close();

        return listHopDong;
    }

    public boolean sendHopDongToNguoiThue(HopDong hopDong){
        SQLiteDatabase db = dBhelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("Phong_ID", hopDong.getPhongId());
        values.put("NguoiThue", hopDong.getUserId());
        values.put("ChuTro", hopDong.getChuTro());
        values.put("PhiDichVu_ID", hopDong.getPhiDichVuId());
        values.put("NgayTaoHopDong", hopDong.getNgayTaoHopDong());
        values.put("NgayKetThuc", hopDong.getNgayKetThuc());
        values.put("TrangThaiHopDong", hopDong.getTrangThaiHopDong());

        long result = db.insert("HopDong", null, values);
        db.close();

        return result > 0;
    }

    public boolean kyHopDong(int hopDongID){
        SQLiteDatabase db = dBhelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("TrangThaiHopDong", 1);

        int result = db.update("HopDong", values, "HopDong_ID = ?", new String[]{String.valueOf(hopDongID)});
        db.close();

        return result > 0;
    }

    public boolean huyHopDong(int hopDongID){
        SQLiteDatabase db = dBhelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("TrangThaiHopDong", 0);

        int result = db.update("HopDong", values, "HopDong_ID = ?", new String[]{String.valueOf(hopDongID)});
        db.close();

        return result > 0;
    }


    public boolean updateHopDong(HopDong hopDong) {
        SQLiteDatabase db = dBhelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("NguoiThue", hopDong.getUserId());
        values.put("ChuTro", hopDong.getChuTro());
        values.put("PhiDichVu_ID", hopDong.getPhiDichVuId());
        values.put("NgayTaoHopDong", hopDong.getNgayTaoHopDong());
        values.put("NgayKetThuc", hopDong.getNgayKetThuc());
        values.put("TrangThaiHopDong", hopDong.getTrangThaiHopDong());

        int rowsAffected = db.update("HopDong", values, "HopDong_ID = ?", new String[]{String.valueOf(hopDong.getHopDongId())});

        db.close();

        return rowsAffected > 0;
    }

    public boolean checkHopDongByPhongID(int phongID) {
        SQLiteDatabase db = dBhelper.getReadableDatabase();
        String query = "SELECT 1 FROM HopDong WHERE Phong_ID = ? LIMIT 1";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(phongID)});
        boolean exists = false;

        if (cursor != null) {
            exists = cursor.moveToFirst();
            cursor.close();
        }

        db.close();
        return exists;
    }


    public boolean capNhatTrangThaiHopDongHetHan(int hopDongId) {
        SQLiteDatabase db = dBhelper.getWritableDatabase();

        // Câu lệnh SQL để cập nhật trạng thái hợp đồng thành 0 (hết hạn)
        ContentValues values = new ContentValues();
        values.put("TrangThaiHopDong", 0); // Trạng thái 0 = hết hạn

        // Cập nhật trạng thái hợp đồng trong cơ sở dữ liệu
        int rowsAffected = db.update("HopDong", values, "HopDong_ID = ?", new String[]{String.valueOf(hopDongId)});

        db.close(); // Đóng kết nối cơ sở dữ liệu

        // Trả về true nếu có dòng bị ảnh hưởng (tức là hợp đồng đã được cập nhật), ngược lại false
        return rowsAffected > 0;
    }


    public HopDong getHopDongDangHieuLucByPhongID(int phongID) {
        SQLiteDatabase db = dBhelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM HopDong WHERE Phong_ID = ? AND TrangThaiHopDong = 1",
                new String[]{String.valueOf(phongID)}
        );

        if (cursor != null && cursor.moveToFirst()) {
            HopDong hopDong = new HopDong(
                    cursor.getInt(cursor.getColumnIndexOrThrow("HopDong_ID")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("Phong_ID")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("NguoiThue")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("ChuTro")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("PhiDichVu_ID")),
                    cursor.getString(cursor.getColumnIndexOrThrow("NgayTaoHopDong")),
                    cursor.getString(cursor.getColumnIndexOrThrow("NgayKetThuc")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("TrangThaiHopDong"))
            );
            cursor.close();
            return hopDong;
        }

        if (cursor != null) cursor.close();
        return null;
    }

}
