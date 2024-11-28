package group1.pro1122.duan1.daos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import group1.pro1122.duan1.database.DBhelper;
import group1.pro1122.duan1.models.ChiPhi;

public class ThuChiDAO {
    private final DBhelper dBhelper;

    public ThuChiDAO(Context context) {
        dBhelper = new DBhelper(context);
    }


    public ArrayList<ChiPhi> getChiPhiByChuSoHuu(int chuSoHuu) {
        ArrayList<ChiPhi> chiPhiList = new ArrayList<>();
        SQLiteDatabase db = dBhelper.getReadableDatabase();

        String query = "SELECT c.* " +
                "FROM ChiPhi c " +
                "JOIN Phong p ON c.Phong_ID = p.Phong_ID " +
                "WHERE p.ChuSoHuu = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(chuSoHuu)});

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int chiPhiID = cursor.getInt(cursor.getColumnIndexOrThrow("ChiPhi_ID"));
                int phongID = cursor.getInt(cursor.getColumnIndexOrThrow("Phong_ID"));
                String tenChiPhi = cursor.getString(cursor.getColumnIndexOrThrow("TenChiPhi"));
                int soTienChi = cursor.getInt(cursor.getColumnIndexOrThrow("SoTienChi"));
                String ngayPhatSinh = cursor.getString(cursor.getColumnIndexOrThrow("NgayPhatSinh"));

                // Thêm đối tượng ChiPhi vào danh sách
                chiPhiList.add(new ChiPhi(
                        chiPhiID,
                        phongID,
                        tenChiPhi,
                        soTienChi,
                        ngayPhatSinh
                ));
            }
            cursor.close();
        }

        db.close();
        return chiPhiList;
    }

    public boolean addChiPhi(ChiPhi chiPhi) {
        SQLiteDatabase db = dBhelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Phong_ID", chiPhi.getPhongId());
        values.put("TenChiPhi", chiPhi.getTenChiPhi());
        values.put("SoTienChi", chiPhi.getSoTienChi());
        values.put("NgayPhatSinh", chiPhi.getNgayPhatSinh());

        long result = db.insert("ChiPhi", null, values);
        db.close();
        return result != -1;
    }

    public boolean updateChiPhi(ChiPhi chiPhi) {
        SQLiteDatabase db = dBhelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("TenChiPhi", chiPhi.getTenChiPhi());
        values.put("SoTienChi", chiPhi.getSoTienChi());
        values.put("Phong_ID", chiPhi.getPhongId());
        String whereClause = "ChiPhi_ID = ?";
        String[] whereArgs = {String.valueOf(chiPhi.getChiPhiId())};

        int result = db.update("ChiPhi", values, whereClause, whereArgs);
        db.close();
        return result > 0;
    }


    public boolean deleteChiPhi(int chiPhiId) {
        SQLiteDatabase db = dBhelper.getWritableDatabase();
        String whereClause = "ChiPhi_ID = ?";
        String[] whereArgs = {String.valueOf(chiPhiId)};
        int result = db.delete("ChiPhi", whereClause, whereArgs);
        db.close();
        return result > 0;
    }




}
