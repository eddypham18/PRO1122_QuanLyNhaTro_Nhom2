package group1.pro1122.duan1.models;

public class ThanhToan {
    private int thanhToanId;
    private int hopDongId;
    private String ngayThanhToan;
    private int soTienThanhToan;
    private int trangThaiThanhToan;

    public ThanhToan(int thanhToanId, int hopDongId, String ngayThanhToan, int soTienThanhToan, int trangThaiThanhToan) {
        this.thanhToanId = thanhToanId;
        this.hopDongId = hopDongId;
        this.ngayThanhToan = ngayThanhToan;
        this.soTienThanhToan = soTienThanhToan;
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

    public String getNgayThanhToan() {
        return ngayThanhToan;
    }

    public void setNgayThanhToan(String ngayThanhToan) {
        this.ngayThanhToan = ngayThanhToan;
    }

    public int getSoTienThanhToan() {
        return soTienThanhToan;
    }

    public void setSoTienThanhToan(int soTienThanhToan) {
        this.soTienThanhToan = soTienThanhToan;
    }

    public int getTrangThaiThanhToan() {
        return trangThaiThanhToan;
    }

    public void setTrangThaiThanhToan(int trangThaiThanhToan) {
        this.trangThaiThanhToan = trangThaiThanhToan;
    }
}
