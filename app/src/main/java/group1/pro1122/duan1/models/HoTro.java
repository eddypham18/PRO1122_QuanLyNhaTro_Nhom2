package group1.pro1122.duan1.models;

public class HoTro {
    private int hoTroId;
    private int userId;
    private String tieuDe;
    private String noiDung;
    private int trangThai;
    private String ngayTao;
    private String ngayXuLy;

    public HoTro(int hoTroId, int userId, String tieuDe, String noiDung, int trangThai, String ngayTao, String ngayXuLy) {
        this.hoTroId = hoTroId;
        this.userId = userId;
        this.tieuDe = tieuDe;
        this.noiDung = noiDung;
        this.trangThai = trangThai;
        this.ngayTao = ngayTao;
        this.ngayXuLy = ngayXuLy;
    }

    public int getHoTroId() {
        return hoTroId;
    }

    public void setHoTroId(int hoTroId) {
        this.hoTroId = hoTroId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTieuDe() {
        return tieuDe;
    }

    public void setTieuDe(String tieuDe) {
        this.tieuDe = tieuDe;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }

    public String getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(String ngayTao) {
        this.ngayTao = ngayTao;
    }

    public String getNgayXuLy() {
        return ngayXuLy;
    }

    public void setNgayXuLy(String ngayXuLy) {
        this.ngayXuLy = ngayXuLy;
    }
}
