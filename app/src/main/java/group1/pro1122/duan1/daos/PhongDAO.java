package group1.pro1122.duan1.daos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import group1.pro1122.duan1.database.DBhelper;
import group1.pro1122.duan1.models.Phong;
public class PhongDAO {
    private final DBhelper dbHelper;

    public PhongDAO(Context context) {
        dbHelper = new DBhelper(context);
    }

    public ArrayList<Phong> read(int userID) {
        ArrayList<Phong> listPhong = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT * FROM Phong WHERE ChuSoHuu = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userID)});

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int phongID = cursor.getInt(cursor.getColumnIndexOrThrow("Phong_ID"));
                int chuSoHuu = cursor.getInt(cursor.getColumnIndexOrThrow("ChuSoHuu"));
                int pheDuyetBoi = cursor.getInt(cursor.getColumnIndexOrThrow("PheDuyetBoi"));
                String soPhong = cursor.getString(cursor.getColumnIndexOrThrow("SoPhong"));
                int dienTich = cursor.getInt(cursor.getColumnIndexOrThrow("DienTich"));
                int trangThaiThue = cursor.getInt(cursor.getColumnIndexOrThrow("TrangThai"));
                int giaThue = cursor.getInt(cursor.getColumnIndexOrThrow("GiaThue"));
                byte[] anhPhong = cursor.getBlob(cursor.getColumnIndexOrThrow("AnhPhong"));
                String moTa = cursor.getString(cursor.getColumnIndexOrThrow("MoTa"));
                String ngayTao = cursor.getString(cursor.getColumnIndexOrThrow("NgayTao"));
                int trangThaiPheDuyet = cursor.getInt(cursor.getColumnIndexOrThrow("TrangThaiPheDuyet"));
                int diaDiemID = cursor.getInt(cursor.getColumnIndexOrThrow("DiaDiem_ID"));

                // Thêm đối tượng Phong vào danh sách
                listPhong.add(new Phong(
                        phongID,
                        chuSoHuu,
                        pheDuyetBoi,
                        soPhong,
                        dienTich,
                        trangThaiThue, // 1: đã thuê, 0: trống
                        giaThue,
                        anhPhong,
                        moTa,
                        ngayTao,
                        trangThaiPheDuyet,// 1: đã duyệt, 0: chưa duyệt
                        diaDiemID
                ));
            }
            cursor.close();
        }

        db.close();
        return listPhong;
    }


    public boolean create(int chuSoHuu, String soPhong, int dienTich, int giaThue, String moTa, byte[] anhPhong, int diaDiem) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("ChuSoHuu", chuSoHuu);
        values.put("SoPhong", soPhong);
        values.put("DienTich", dienTich);
        values.put("TrangThai", 0); // Mặc định là trống
        values.put("GiaThue", giaThue);
        values.put("AnhPhong", anhPhong); // Lưu ảnh dưới dạng byte[]
        values.put("MoTa", moTa);
        values.put("NgayTao", String.valueOf(System.currentTimeMillis())); // Lưu timestamp dưới dạng chuỗi
        values.put("TrangThaiPheDuyet", 0); // Mặc định là chưa duyệt
        values.put("DiaDiem_ID", diaDiem);

        long result = db.insert("Phong", null, values);
        db.close();

        return result != -1;
    }

    // PhongDAO.java
    public boolean delete(int phongID) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int result = db.delete("Phong", "Phong_ID = ?", new String[]{String.valueOf(phongID)});
        db.close();
        return result > 0; // Trả về true nếu xóa thành công
    }

    public boolean update(Phong phong) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("SoPhong", phong.getSoPhong());
        values.put("DienTich", phong.getDienTich());
        values.put("GiaThue", phong.getGiaThue());
        values.put("MoTa", phong.getMoTa());
        values.put("AnhPhong", phong.getAnhPhong()); // Cập nhật ảnh dưới dạng byte[]
        values.put("DiaDiem_ID", phong.getDiaDiemID());

        // Điều kiện để xác định bản ghi sẽ cập nhật
        String whereClause = "Phong_ID = ?";
        String[] whereArgs = {String.valueOf(phong.getPhongId())};

        // Thực hiện cập nhật
        int result = db.update("Phong", values, whereClause, whereArgs);
        db.close();

        // Trả về true nếu cập nhật thành công, false nếu ngược lại
        return result > 0;
    }

}
