package group1.pro1122.duan1.models;

public class PhiDichVu {
    private int phiDichVuId;
    private int donGiaDien;
    private int donGiaNuoc;
    private int veSinh;
    private Integer guiXe;
    private int internet;
    private Integer thangMay;

    public PhiDichVu(int phiDichVuId, int donGiaDien, int donGiaNuoc, int veSinh, Integer guiXe, int internet, Integer thangMay) {
        this.phiDichVuId = phiDichVuId;
        this.donGiaDien = donGiaDien;
        this.donGiaNuoc = donGiaNuoc;
        this.veSinh = veSinh;
        this.guiXe = guiXe;
        this.internet = internet;
        this.thangMay = thangMay;
    }

    public int getPhiDichVuId() {
        return phiDichVuId;
    }

    public void setPhiDichVuId(int phiDichVuId) {
        this.phiDichVuId = phiDichVuId;
    }

    public int getDonGiaDien() {
        return donGiaDien;
    }

    public void setDonGiaDien(int donGiaDien) {
        this.donGiaDien = donGiaDien;
    }

    public int getDonGiaNuoc() {
        return donGiaNuoc;
    }

    public void setDonGiaNuoc(int donGiaNuoc) {
        this.donGiaNuoc = donGiaNuoc;
    }

    public int getVeSinh() {
        return veSinh;
    }

    public void setVeSinh(int veSinh) {
        this.veSinh = veSinh;
    }

    public Integer getGuiXe() {
        return guiXe;
    }

    public void setGuiXe(Integer guiXe) {
        this.guiXe = guiXe;
    }

    public int getInternet() {
        return internet;
    }

    public void setInternet(int internet) {
        this.internet = internet;
    }

    public Integer getThangMay() {
        return thangMay;
    }

    public void setThangMay(Integer thangMay) {
        this.thangMay = thangMay;
    }
}
