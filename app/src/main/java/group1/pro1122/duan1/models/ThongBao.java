package group1.pro1122.duan1.models;

public class ThongBao {
    private int thongBaoId;
    private int userId;
    private Integer hopDongId;
    private int loaiThongBao;
    private String noiDung;
    private String ngayGuiThongBao;

    public ThongBao(int thongBaoId, int userId, Integer hopDongId, int loaiThongBao, String noiDung, String ngayGuiThongBao) {
        this.thongBaoId = thongBaoId;
        this.userId = userId;
        this.hopDongId = hopDongId;
        this.loaiThongBao = loaiThongBao;
        this.noiDung = noiDung;
        this.ngayGuiThongBao = ngayGuiThongBao;
    }

    public int getThongBaoId() {
        return thongBaoId;
    }

    public void setThongBaoId(int thongBaoId) {
        this.thongBaoId = thongBaoId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Integer getHopDongId() {
        return hopDongId;
    }

    public void setHopDongId(Integer hopDongId) {
        this.hopDongId = hopDongId;
    }

    public int getLoaiThongBao() {
        return loaiThongBao;
    }

    public void setLoaiThongBao(int loaiThongBao) {
        this.loaiThongBao = loaiThongBao;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public String getNgayGuiThongBao() {
        return ngayGuiThongBao;
    }

    public void setNgayGuiThongBao(String ngayGuiThongBao) {
        this.ngayGuiThongBao = ngayGuiThongBao;
    }
}
