package group1.pro1122.duan1.models;

public class Phong {
    private int phongId;
    private int chuSoHuu;
    private Integer pheDuyetBoi;
    private String soPhong;
    private int dienTich;
    private int trangThai;
    private int giaThue;
    private byte[] anhPhong;
    private String moTa;
    private String ngayTao;
    private int trangThaiPheduyet;
    private int diaDiemID;

    public Phong(int phongId, int chuSoHuu, Integer pheDuyetBoi, String soPhong, int dienTich, int trangThai, int giaThue, byte[] anhPhong, String moTa, String ngayTao, int trangThaiPheduyet, int diaDiemID) {
        this.phongId = phongId;
        this.chuSoHuu = chuSoHuu;
        this.pheDuyetBoi = pheDuyetBoi;
        this.soPhong = soPhong;
        this.dienTich = dienTich;
        this.trangThai = trangThai;
        this.giaThue = giaThue;
        this.anhPhong = anhPhong;
        this.moTa = moTa;
        this.ngayTao = ngayTao;
        this.trangThaiPheduyet = trangThaiPheduyet;
        this.diaDiemID = diaDiemID;
    }

    public int getPhongId() {
        return phongId;
    }

    public void setPhongId(int phongId) {
        this.phongId = phongId;
    }

    public int getChuSoHuu() {
        return chuSoHuu;
    }

    public void setChuSoHuu(int chuSoHuu) {
        this.chuSoHuu = chuSoHuu;
    }

    public Integer getPheDuyetBoi() {
        return pheDuyetBoi;
    }

    public void setPheDuyetBoi(Integer pheDuyetBoi) {
        this.pheDuyetBoi = pheDuyetBoi;
    }

    public String getSoPhong() {
        return soPhong;
    }

    public void setSoPhong(String soPhong) {
        this.soPhong = soPhong;
    }

    public int getDienTich() {
        return dienTich;
    }

    public void setDienTich(int dienTich) {
        this.dienTich = dienTich;
    }

    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }

    public int getGiaThue() {
        return giaThue;
    }

    public void setGiaThue(int giaThue) {
        this.giaThue = giaThue;
    }

    public byte[] getAnhPhong() {
        return anhPhong;
    }

    public void setAnhPhong(byte[] anhPhong) {
        this.anhPhong = anhPhong;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(String ngayTao) {
        this.ngayTao = ngayTao;
    }

    public int getTrangThaiPheduyet() {
        return trangThaiPheduyet;
    }

    public void setTrangThaiPheduyet(int trangThaiPheduyet) {
        this.trangThaiPheduyet = trangThaiPheduyet;
    }

    public int getDiaDiemID() {
        return diaDiemID;
    }

    public void setDiaDiemID(int diaDiemID) {
        this.diaDiemID = diaDiemID;
    }
}
