package group1.pro1122.duan1.models;

public class ThanhToan {
    private int thanhToanId;
    private int hopDongId;
    private String ngayTao;
    private int soDien;
    private int soNuoc;
    private int tongTien;
    private int trangThaiThanhToan;

    public ThanhToan(int thanhToanId, int hopDongId, String ngayTao, int soDien, int soNuoc, int tongTien, int trangThaiThanhToan) {
        this.thanhToanId = thanhToanId;
        this.hopDongId = hopDongId;
        this.ngayTao = ngayTao;
        this.soDien = soDien;
        this.soNuoc = soNuoc;
        this.tongTien = tongTien;
        this.trangThaiThanhToan = trangThaiThanhToan;
    }

    public int getThanhToanId() {
        return thanhToanId;
    }

    public void setThanhToanId(int thanhToanId) {
        this.thanhToanId = thanhToanId;
    }

    public int getHopDongId() {
        return hopDongId;
    }

    public void setHopDongId(int hopDongId) {
        this.hopDongId = hopDongId;
    }

    public String getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(String ngayTao) {
        this.ngayTao = ngayTao;
    }

    public int getSoDien() {
        return soDien;
    }

    public void setSoDien(int soDien) {
        this.soDien = soDien;
    }

    public int getSoNuoc() {
        return soNuoc;
    }

    public void setSoNuoc(int soNuoc) {
        this.soNuoc = soNuoc;
    }

    public int getTongTien() {
        return tongTien;
    }

    public void setTongTien(int tongTien) {
        this.tongTien = tongTien;
    }

    public int getTrangThaiThanhToan() {
        return trangThaiThanhToan;
    }

    public void setTrangThaiThanhToan(int trangThaiThanhToan) {
        this.trangThaiThanhToan = trangThaiThanhToan;
    }
}
