package group1.pro1122.duan1.daos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import group1.pro1122.duan1.database.DBhelper;
import group1.pro1122.duan1.models.DiaDiem;
import group1.pro1122.duan1.models.User;

public class DiaDiemDAO {

    private final DBhelper dBhelper;

    public DiaDiemDAO(Context context) {
        this.dBhelper = new DBhelper(context);
    }

    public ArrayList<DiaDiem> read(){
        ArrayList<DiaDiem> list = new ArrayList<>();
        SQLiteDatabase database = dBhelper.getReadableDatabase();

        database.beginTransaction();
        try {
            Cursor cursor = database.rawQuery("SELECT * FROM DiaDiem", null);
            if(cursor.getColumnCount() > 0){
                //Nếu cursor lớn hơn 0 di chuyển con trỏ lên đầu
                cursor.moveToFirst();
                //Khởi tạo vòng lặp để lấy dữ liệu
                do {
                    list.add(new DiaDiem(
                            cursor.getInt(0),
                            cursor.getString(1)
                    ));
                } while (cursor.moveToNext());
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            database.endTransaction();
        }
        return list;
    }

    public DiaDiem getDiaDiemByDiaDiemID(int diaDiemID){
        DiaDiem diaDiem = null;
        SQLiteDatabase database = dBhelper.getReadableDatabase();
        database.beginTransaction();
        try {
            Cursor cursor = database.rawQuery("SELECT * FROM DiaDiem WHERE DiaDiem_ID = ?", new String[]{String.valueOf(diaDiemID)});
            if(cursor.getColumnCount() > 0){
                cursor.moveToFirst();
                diaDiem = new DiaDiem(
                        cursor.getInt(0),
                        cursor.getString(1)
                );
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            database.endTransaction();
        }
        return diaDiem;
    }
}
