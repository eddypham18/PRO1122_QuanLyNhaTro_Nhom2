package group1.pro1122.duan1.models;

public class ChiPhi {
    private int chiPhiId;
    private int phongId;
    private String tenChiPhi;
    private int soTienChi;
    private String ngayPhatSinh;

    public ChiPhi(int chiPhiId, int phongId, String tenChiPhi, int soTienChi, String ngayPhatSinh) {
        this.chiPhiId = chiPhiId;
        this.phongId = phongId;
        this.tenChiPhi = tenChiPhi;
        this.soTienChi = soTienChi;
        this.ngayPhatSinh = ngayPhatSinh;
    }

    public int getChiPhiId() {
        return chiPhiId;
    }

    public void setChiPhiId(int chiPhiId) {
        this.chiPhiId = chiPhiId;
    }

    public int getPhongId() {
        return phongId;
    }

    public void setPhongId(int phongId) {
        this.phongId = phongId;
    }

    public String getTenChiPhi() {
        return tenChiPhi;
    }

    public void setTenChiPhi(String tenChiPhi) {
        this.tenChiPhi = tenChiPhi;
    }

    public int getSoTienChi() {
        return soTienChi;
    }

    public void setSoTienChi(int soTienChi) {
        this.soTienChi = soTienChi;
    }

    public String getNgayPhatSinh() {
        return ngayPhatSinh;
    }

    public void setNgayPhatSinh(String ngayPhatSinh) {
        this.ngayPhatSinh = ngayPhatSinh;
    }
}
