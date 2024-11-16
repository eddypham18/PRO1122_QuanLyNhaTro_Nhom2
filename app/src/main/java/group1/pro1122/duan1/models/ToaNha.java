package group1.pro1122.duan1.models;

public class ToaNha {
    private int toaNhaID;
    private int userId;
    private int diaDiemId;
    private String tenToaNha;

    private int soLuongPhong;

    public ToaNha(int toaNhaID, int userId, int diaDiemId, String tenToaNha, int soLuongPhong) {
        this.toaNhaID = toaNhaID;
        this.userId = userId;
        this.diaDiemId = diaDiemId;
        this.tenToaNha = tenToaNha;
        this.soLuongPhong = soLuongPhong;
    }

    public int getToaNhaID() {
        return toaNhaID;
    }

    public void setToaNhaID(int toaNhaID) {
        this.toaNhaID = toaNhaID;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getDiaDiemId() {
        return diaDiemId;
    }

    public void setDiaDiemId(int diaDiemId) {
        this.diaDiemId = diaDiemId;
    }

    public String getTenToaNha() {
        return tenToaNha;
    }

    public void setTenToaNha(String tenToaNha) {
        this.tenToaNha = tenToaNha;
    }

    public int getSoLuongPhong() {
        return soLuongPhong;
    }

    public void setSoLuongPhong(int soLuongPhong) {
        this.soLuongPhong = soLuongPhong;
    }
}
