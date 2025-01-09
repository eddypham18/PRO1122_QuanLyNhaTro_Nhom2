package group1.pro1122.duan1.daos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import java.util.ArrayList;

import group1.pro1122.duan1.database.DBhelper;
import group1.pro1122.duan1.models.User;

public class UserDAO {

    private final DBhelper dBhelper;

    public UserDAO(Context context){
        this.dBhelper = new DBhelper(context);
    }
    public ArrayList<User> read(){
        ArrayList<User> list = new ArrayList<>();
        SQLiteDatabase database = dBhelper.getReadableDatabase();
        database.beginTransaction();
        try {
            Cursor cursor = database.rawQuery("SELECT * FROM User", null);
            if(cursor.getColumnCount() > 0){
                //Nếu cursor lớn hơn 0 di chuyển con trỏ lên đầu
                cursor.moveToFirst();
                //Khởi tạo vòng lặp để lấy dữ liệu
                do {
                    list.add(new User(
                            cursor.getInt(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getInt(3),
                            cursor.getString(4),
                            cursor.getString(5),
                            cursor.getString(6),
                            cursor.getString(7),
                            cursor.getInt(8),
                            cursor.getString(9),
                            cursor.getString(10)
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


    public boolean registerUser(User user) {
        SQLiteDatabase database = dBhelper.getWritableDatabase();
        database.beginTransaction();
        try {
            String query = "INSERT INTO User (Username, Password, VaiTro, SDT, TrangThai, NgayTao) VALUES (?, ?, ?, ?, ?, ?)";
            Object[] values = {
                    user.getUsername(),
                    user.getPassword(),
                    user.getVaiTro(),
                    user.getSdt(),
                    user.getTrangThai(),
                    user.getNgayTao()
            };

            database.execSQL(query, values);
            database.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            database.endTransaction();
        }
    }
    //Kiểm tra sự tồn tại của username
    public boolean isUsernameExist(String username) {
        SQLiteDatabase db = dBhelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM User WHERE username = ?", new String[]{username});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    //Update mật khẩu dựa vào username
    public boolean updatePassword(String username, String newPassword) {
        SQLiteDatabase db = dBhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("password", newPassword);

        int result = db.update("User", contentValues, "username = ?", new String[]{username});

        return result > 0;
    }


    // Cập nhật trạng thái hoạt động của người dùng
    public boolean updateTrangThai(int userId, int trangThai) {
        SQLiteDatabase db = dBhelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("trangThai", trangThai);
        return db.update("User", values, "User_ID = ?", new String[]{String.valueOf(userId)}) > 0;
    }


    public User getUserByUserID(int userId) {
        SQLiteDatabase db = dBhelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM User WHERE User_ID = ?", new String[]{String.valueOf(userId)});

        User user = null;  // Biến để lưu đối tượng User

        if (cursor != null && cursor.moveToFirst()) {
            // Lấy thông tin từ cursor và tạo đối tượng User
            user = new User(
                    cursor.getInt(cursor.getColumnIndexOrThrow("User_ID")),
                    cursor.getString(cursor.getColumnIndexOrThrow("Username")),
                    cursor.getString(cursor.getColumnIndexOrThrow("Password")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("VaiTro")),
                    cursor.getString(cursor.getColumnIndexOrThrow("HoTen")),
                    cursor.getString(cursor.getColumnIndexOrThrow("NgaySinh")),
                    cursor.getString(cursor.getColumnIndexOrThrow("Email")),
                    cursor.getString(cursor.getColumnIndexOrThrow("SDT")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("TrangThai")),
                    cursor.getString(cursor.getColumnIndexOrThrow("CCCD")),
                    cursor.getString(cursor.getColumnIndexOrThrow("NgayTao"))
            );
        }

        cursor.close();  // Đóng cursor sau khi sử dụng
        db.close();  // Đóng kết nối cơ sở dữ liệu

        return user;  // Trả về đối tượng User nếu tìm thấy, hoặc null nếu không tồn tại
    }

    public boolean updateUserInfo(User user) {
        SQLiteDatabase db = dBhelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("HoTen", user.getHoTen());
        values.put("NgaySinh", user.getNgaySinh());
        values.put("Email", user.getEmail());
        values.put("SDT", user.getSdt());
        values.put("CCCD", user.getCccd());

        int result = db.update("User", values, "User_ID = ?", new String[]{String.valueOf(user.getUserId())});
        db.close();
        return result > 0;
    }


    public boolean checkCCCD(String cccd) {
        SQLiteDatabase db = dBhelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM User WHERE CCCD = ?", new String[]{cccd});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

}
