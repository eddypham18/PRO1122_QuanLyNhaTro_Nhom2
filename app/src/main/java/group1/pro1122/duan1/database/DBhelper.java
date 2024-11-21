package group1.pro1122.duan1.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

import group1.pro1122.duan1.R;

public class DBhelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "DuAn1";
    private static final int DATABASE_VERSION = 22;
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
                "CCCD TEXT," +
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
                " Phong_ID INTEGER," +
                " TenChiPhi TEXT," +
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
                " NoiDung TEXT NOT NULL," +
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
                " TrangThai INTEGER NOT NULL," +
                " FOREIGN KEY (User_ID) REFERENCES User(User_ID)," +
                " FOREIGN KEY (HopDong_ID) REFERENCES HopDong(HopDong_ID))"
        );

        // Bảng ThanhToan
        db.execSQL("CREATE TABLE ThanhToan (" +
                " ThanhToan_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " HopDong_ID INTEGER NOT NULL," +
                " NgayTao TEXT NOT NULL," +
                " soDienTieuThu INTEGER NOT NULL,"+
                " soNuocTieuThu INTEGER NOT NULL,"+
                " TongTien INTEGER NOT NULL," +
                " TrangThaiThanhToan INTEGER NOT NULL," +
                " FOREIGN KEY (HopDong_ID) REFERENCES HopDong(HopDong_ID))"
        );

        // Bảng User
        db.execSQL("INSERT INTO User (Username, Password, VaiTro, HoTen, NgaySinh, Email, SDT, TrangThai, CCCD, NgayTao) VALUES " +
                "('nguoidung', 'password', 0, 'Nguyen Van A', '01-01-1990', 'admin01@example.com', '0123456789', 1, '123456789101', '2024-01-01')," +
                "('chutro', 'password', 1, 'Le Thi B', '05-05-1995', 'user01@example.com', '0987654321', 1, '987654321012', '2024-01-10')," +
                "('admin', 'admin', 2, 'Tran Van C', '08-08-1999', 'user02@example.com', '0911223344', 1, '456789123123', '2024-02-15')," +
                "('user3', 'password', 0, 'Pham Van D', '12-12-1992', 'user03@example.com', '0922334455', 1, '321654987123', '2024-03-20')," +
                "('user4', 'password', 1, 'Nguyen Thi E', '02-02-1988', 'user04@example.com', '0933445566', 0, '789123456321', '2024-04-25')," +
                "('user5', 'password', 0, 'Tran Van F', '10-10-1985', 'user05@example.com', '0944556677', 0, '654321987456', '2024-05-30')," +
                "('user6', 'password', 1, 'Le Van G', '06-06-1991', 'user06@example.com', '0955667788', 1, '123987654321', '2024-06-05')," +
                "('user7', 'password', 2, 'Hoang Thi H', '07-07-1993', 'user07@example.com', '0966778899', 1, '987123654321', '2024-07-10')," +
                "('user8', 'password', 0, 'Pham Thi I', '03-03-1994', 'user08@example.com', '0977889900', 0, '321123987654', '2024-08-15')," +
                "('user9', 'password', 1, 'Nguyen Van J', '09-09-1989', 'user09@example.com', '0988990011', 1, '789456123789', '2024-09-20')," +
                "('user10', 'password', 0, 'Le Thi K', '04-04-1996', 'user10@example.com', '0999001122', 1, '456789654123', '2024-10-25');");

// Bảng DiaDiem
        db.execSQL("INSERT INTO DiaDiem (ThanhPho) VALUES " +
                "('Hà Nội')," +
                "('TP. Hồ Chí Minh')," +
                "('Đà Nẵng')," +
                "('Hải Phòng')," +
                "('Cần Thơ')," +
                "('Nha Trang')," +
                "('Huế')," +
                "('Biên Hòa')," +
                "('Buôn Ma Thuột')," +
                "('Đà Lạt')," +
                "('Vũng Tàu')," +
                "('Quy Nhơn')," +
                "('Phan Thiết')," +
                "('Thái Nguyên')," +
                "('Bắc Ninh')," +
                "('Hạ Long')," +
                "('Pleiku')," +
                "('Mỹ Tho')," +
                "('Bến Tre')," +
                "('Long Xuyên')," +
                "('Rạch Giá')," +
                "('Tuy Hòa')," +
                "('Tam Kỳ')," +
                "('Bạc Liêu')," +
                "('Trà Vinh')," +
                "('Cao Lãnh')," +
                "('Sa Đéc')," +
                "('Phan Rang – Tháp Chàm')," +
                "('Sóc Trăng')," +
                "('Kon Tum')," +
                "('Cà Mau')," +
                "('Yên Bái')," +
                "('Lạng Sơn')," +
                "('Điện Biên Phủ')," +
                "('Hà Giang')," +
                "('Sơn La')," +
                "('Lào Cai')," +
                "('Tuyên Quang')," +
                "('Thái Bình')," +
                "('Nam Định')," +
                "('Vinh')," +
                "('Hòa Bình')," +
                "('Thanh Hóa')," +
                "('Ninh Bình')," +
                "('Hà Tĩnh')," +
                "('Quảng Bình')," +
                "('Quảng Trị')," +
                "('Quảng Ngãi')," +
                "('Bình Định')," +
                "('Phú Yên')," +
                "('Gia Lai')," +
                "('Bình Dương')," +
                "('Bình Phước')," +
                "('Tây Ninh')," +
                "('Hậu Giang')," +
                "('Đồng Tháp')," +
                "('An Giang')," +
                "('Kiên Giang')," +
                "('Vĩnh Long')," +
                "('Bình Thuận')," +
                "('Lâm Đồng')," +
                "('Đồng Nai')," +
                "('Long An');");

        // Bảng Phong
        db.execSQL("INSERT INTO Phong (ChuSoHuu, PheDuyetBoi, SoPhong, DienTich, TrangThai, GiaThue, AnhPhong, MoTa, NgayTao, TrangThaiPheDuyet, DiaDiem_ID) VALUES " +
                "(2, 3, 'P101', 25, 0, 3000000, X'89504E470D0A1A0A', 'Phòng khép kín, không chung chủ, giờ giấc tự do. Gần trường CĐ FPT Polytechnic. Liên hệ để biết thêm thông tin: 0931.333.399', 1704067200, 0, 1)," +
                "(2, 3, 'P102', 20, 0, 2500000, X'89504E470D0A1A0A', 'Phòng mới xây, có gác lửng, tiện nghi đầy đủ. Gần chợ và các tiện ích. Liên hệ: 0932.444.400', 1704153600, 0, 2)," +
                "(2, 3, 'P103', 30, 0, 4000000, X'89504E470D0A1A0A', 'Phòng rộng, thoáng mát, có ban công và chỗ để xe miễn phí. Gần công viên và khu dân cư an ninh. Liên hệ: 0911.555.501', 1704240000, 0, 3)," +
                "(2, 3, 'P104', 18, 0, 2200000, X'89504E470D0A1A0A', 'Phòng nhỏ gọn, phù hợp cho sinh viên, gần đại học Bách Khoa. Internet tốc độ cao miễn phí. Liên hệ: 0987.666.602', 1704326400, 0, 4)," +
                "(2, 3, 'P105', 28, 0, 3700000, X'89504E470D0A1A0A', 'Phòng có điều hòa, máy nước nóng, tiện nghi hiện đại. Cách trung tâm thành phố 10 phút đi xe. Liên hệ: 0976.777.703', 1704412800, 0, 1);");

// Bảng ChiPhi
        db.execSQL("INSERT INTO ChiPhi (Phong_ID, TenChiPhi, SoTienChi, NgayPhatSinh) VALUES " +
                "(1, 'Bảo dưỡng điều hòa', 200000, 1707955200)," +
                "(2, 'Sửa TV', 150000, 1708041600)," +
                "(3, 'Vệ sinh phòng', 90000, 1708128000)," +
                "(4, 'Mua giường mới', 2500000, 1708214400)," +
                "(5, 'Sửa đường nước', 400000, 1708300800);");



// Bảng PhiDichVu
        db.execSQL("INSERT INTO PhiDichVu (DonGiaDien, DonGiaNuoc, VeSinh, GuiXe, Internet, ThangMay) VALUES " +
                "(3000, 4000, 20000, 50000, 100000, 15000)," +
                "(3500, 4500, 25000, 55000, 120000, 20000)," +
                "(3200, 4200, 23000, 53000, 110000, 18000);");

// Bảng HoTro
        db.execSQL("INSERT INTO HoTro (User_ID, TieuDe, NoiDung, TrangThai, NgayTao, NgayXuLy) VALUES " +
                "(2, 'Tính năng thống kê', 'Hãy cập nhật lại chức năng thống kê để tôi có thể tính lợi nhuận. Cảm ơn', 1, 1704067200, 1704499200)," +
                "(2, 'Ứng dụng lag', 'Khi tôi vào quản lý hợp đồng bị lag', 1, 1704412800, 1704499200)," +
                "(1, 'Quản lý thanh toán', 'Cần hướng dẫn tạo phiếu thanh toán - email: tranvantoan@gmail.com', 0, 1704844800, NULL)," +
                "(5, 'Trang chủ', 'Trang chủ ít phòng quá', 1, 1705276800, 1705363200)," +
                "(4, 'Hỗ trợ tài khoản', 'Làm thế nào để tôi có thể đổi mật khẩu tài khoản của mình', 0, 1705708800, NULL);");


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
