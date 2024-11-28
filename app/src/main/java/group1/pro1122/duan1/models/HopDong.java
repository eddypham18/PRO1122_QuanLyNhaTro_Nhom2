package group1.pro1122.duan1.models;

public class HopDong {
    private int hopDongId;
    private int phongId;
    private int userId;
    private int chuTro;
    private int phiDichVuId;
    private String ngayTaoHopDong;
    private String ngayKetThuc;
    private int trangThaiHopDong;

    public HopDong(int hopDongId, int phongId, int userId, int chuTro, int phiDichVuId, String ngayTaoHopDong, String ngayKetThuc, int trangThaiHopDong) {
        this.hopDongId = hopDongId;
        this.phongId = phongId;
        this.userId = userId;
        this.chuTro = chuTro;
        this.phiDichVuId = phiDichVuId;
        this.ngayTaoHopDong = ngayTaoHopDong;
        this.ngayKetThuc = ngayKetThuc;
        this.trangThaiHopDong = trangThaiHopDong;
    }

    public int getHopDongId() {
        return hopDongId;
    }

    public void setHopDongId(int hopDongId) {
        this.hopDongId = hopDongId;
    }

    public int getPhongId() {
        return phongId;
    }

    public void setPhongId(int phongId) {
        this.phongId = phongId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getChuTro() {
        return chuTro;
    }

    public void setChuTro(int chuTro) {
        this.chuTro = chuTro;
    }

    public int getPhiDichVuId() {
        return phiDichVuId;
    }

    public void setPhiDichVuId(int phiDichVuId) {
        this.phiDichVuId = phiDichVuId;
    }

    public String getNgayTaoHopDong() {
        return ngayTaoHopDong;
    }

    public void setNgayTaoHopDong(String ngayTaoHopDong) {
        this.ngayTaoHopDong = ngayTaoHopDong;
    }

    public String getNgayKetThuc() {
        return ngayKetThuc;
    }

    public void setNgayKetThuc(String ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }

    public int getTrangThaiHopDong() {
        return trangThaiHopDong;
    }

    public void setTrangThaiHopDong(int trangThaiHopDong) {
        this.trangThaiHopDong = trangThaiHopDong;
    }
}
