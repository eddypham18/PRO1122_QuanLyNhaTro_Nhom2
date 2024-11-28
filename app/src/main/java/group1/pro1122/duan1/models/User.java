package group1.pro1122.duan1.models;

public class User {
    private int userId;
    private String username;
    private String password;
    private int vaiTro;
    private String hoTen;
    private String ngaySinh;
    private String email;
    private String sdt;
    private int trangThai;
    private String cccd;
    private String ngayTao;

    public User() {

    }

    public User(int userId, String username, String password, int vaiTro, String hoTen, String ngaySinh, String email, String sdt, int trangThai, String cccd, String ngayTao) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.vaiTro = vaiTro;
        this.hoTen = hoTen;
        this.ngaySinh = ngaySinh;
        this.email = email;
        this.sdt = sdt;
        this.trangThai = trangThai;
        this.cccd = cccd;
        this.ngayTao = ngayTao;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getVaiTro() {
        return vaiTro;
    }

    public void setVaiTro(int vaiTro) {
        this.vaiTro = vaiTro;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(String ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }

    public String getCccd() {
        return cccd;
    }

    public void setCccd(String cccd) {
        this.cccd = cccd;
    }

    public String getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(String ngayTao) {
        this.ngayTao = ngayTao;
    }
}