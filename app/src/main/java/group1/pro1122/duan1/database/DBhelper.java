package group1.pro1122.duan1.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBhelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "DuAn1";
    private static final int DATABASE_VERSION = 9;

    public DBhelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Bảng User
        db.execSQL("CREATE TABLE User (" +
                "User_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Username TEXT NOT NULL," +
                "Password TEXT NOT NULL," +
                "VaiTro INTEGER NOT NULL," +
                "HoTen TEXT," +
                "NgaySinh TEXT," +
                "Email TEXT," +
                "SDT TEXT NOT NULL," +
                "TrangThai INTEGER NOT NULL," +
                "CCCD INTEGER," +
                "NgayTao TEXT NOT NULL)"
        );

        // Bảng Phong
        db.execSQL("CREATE TABLE Phong (" +
                " Phong_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " ChuSoHuu INTEGER NOT NULL," +
                " PheDuyetBoi INTEGER," +
                " SoPhong TEXT NOT NULL," +
                " DienTich INTEGER NOT NULL," +
                " TrangThai INTEGER NOT NULL," +
                " GiaThue INTEGER NOT NULL," +
                " AnhPhong BLOB NOT NULL," +
                " MoTa TEXT NOT NULL," +
                " NgayTao TEXT NOT NULL," +
                " TrangThaiPheDuyet INTEGER NOT NULL," +
                " DiaDiem_ID INTEGER NOT NULL,"+
                " FOREIGN KEY (ChuSoHuu) REFERENCES User(User_ID)," +
                " FOREIGN KEY (PheDuyetBoi) REFERENCES User(User_ID),"+
                " FOREIGN KEY (DiaDiem_ID) REFERENCES DiaDiem(DiaDiem_ID))"
        );

        // Bảng DiaDiem
        db.execSQL("CREATE TABLE DiaDiem (" +
                " DiaDiem_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " ThanhPho TEXT NOT NULL)"
        );

        // Bảng ChiPhi
        db.execSQL("CREATE TABLE ChiPhi (" +
                " ChiPhi_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " Phong_ID INTEGER NOT NULL," +
                " LoaiChiPhi TEXT," +
                " SoTienChi INTEGER NOT NULL," +
                " NgayPhatSinh TEXT NOT NULL," +
                " FOREIGN KEY (Phong_ID) REFERENCES Phong(Phong_ID))"
        );

        // Bảng HopDong
        db.execSQL("CREATE TABLE HopDong (" +
                " HopDong_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " Phong_ID INTEGER NOT NULL," +
                " NguoiThue INTEGER NOT NULL," +
                " ChuTro INTEGER NOT NULL," +
                " PhiDichVu_ID INTEGER NOT NULL," +
                " NgayTaoHopDong TEXT NOT NULL," +
                " KyThanhToan INTEGER NOT NULL," +
                " NgayKetThuc TEXT NOT NULL," +
                " TrangThaiHopDong INTEGER NOT NULL," +
                " FOREIGN KEY (Phong_ID) REFERENCES Phong(Phong_ID)," +
                " FOREIGN KEY (NguoiThue) REFERENCES User(User_ID)," +
                " FOREIGN KEY (ChuTro) REFERENCES User(User_ID)," +
                " FOREIGN KEY (PhiDichVu_ID) REFERENCES PhiDichVu(PhiDichVu_ID))"
        );

        // Bảng PhiDichVu
        db.execSQL("CREATE TABLE PhiDichVu (" +
                " PhiDichVu_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " DonGiaDien INTEGER NOT NULL," +
                " DonGiaNuoc INTEGER NOT NULL," +
                " VeSinh INTEGER NOT NULL," +
                " GuiXe INTEGER," +
                " Internet INTEGER NOT NULL," +
                " ThangMay INTEGER)"
        );

        // Bảng HoTro
        db.execSQL("CREATE TABLE HoTro (" +
                " HoTro_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " User_ID INTEGER NOT NULL," +
                " TieuDe TEXT NOT NULL," +
                "  NoiDung TEXT NOT NULL," +
                " TrangThai INTEGER NOT NULL," +
                " NgayTao TEXT NOT NULL," +
                " NgayXuLy TEXT," +
                " FOREIGN KEY (User_ID) REFERENCES User(User_ID))"
        );

        // Bảng ThongBao
        db.execSQL("CREATE TABLE ThongBao (" +
                " ThongBao_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " User_ID INTEGER NOT NULL," +
                " HopDong_ID INTEGER," +
                " LoaiThongBao INTEGER NOT NULL," +
                " NoiDung TEXT NOT NULL," +
                " NgayGuiThongBao TEXT NOT NULL," +
                " FOREIGN KEY (User_ID) REFERENCES User(User_ID)," +
                " FOREIGN KEY (HopDong_ID) REFERENCES HopDong(HopDong_ID))"
        );

        // Bảng ThanhToan
        db.execSQL("CREATE TABLE ThanhToan (" +
                " ThanhToan_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " HopDong_ID INTEGER NOT NULL," +
                " NgayThanhToan TEXT," +
                " SoTienThanhToan INTEGER NOT NULL," +
                " TrangThaiThanhToan INTEGER NOT NULL," +
                " FOREIGN KEY (HopDong_ID) REFERENCES HopDong(HopDong_ID))"
        );

        // Bảng User
        db.execSQL("INSERT INTO User (Username, Password, VaiTro, HoTen, NgaySinh, Email, SDT, TrangThai, CCCD, NgayTao) VALUES " +
                "('user1', 'password', 0, 'Nguyen Van A', '1990-01-01', 'admin01@example.com', '0123456789', 1, '123456789', '2024-01-01')," +
                "('user2', 'password', 1, 'Le Thi B', '1995-05-05', 'user01@example.com', '0987654321', 1, '987654321', '2024-01-10')," +
                "('admin', 'admin', 2, 'Tran Van C', '1998-08-08', 'user02@example.com', '0911223344', 1, '456789123', '2024-02-15');");

// Bảng DiaDiem
        db.execSQL("INSERT INTO DiaDiem (ThanhPho) VALUES " +
                "('Hà Nội')," +
                "('TP. Hồ Chí Minh')," +
                "('Đà Nẵng');");



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion != newVersion){
            db.execSQL("DROP TABLE IF EXISTS User");
            db.execSQL("DROP TABLE IF EXISTS Phong");
            db.execSQL("DROP TABLE IF EXISTS DiaDiem");
            db.execSQL("DROP TABLE IF EXISTS ChiPhi");
            db.execSQL("DROP TABLE IF EXISTS HopDong");
            db.execSQL("DROP TABLE IF EXISTS PhiDichVu");
            db.execSQL("DROP TABLE IF EXISTS HoTro");
            db.execSQL("DROP TABLE IF EXISTS ThongBao");
            db.execSQL("DROP TABLE IF EXISTS ThanhToan");
            onCreate(db);
        }
    }
}
