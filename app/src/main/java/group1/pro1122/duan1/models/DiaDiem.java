package group1.pro1122.duan1.models;

public class DiaDiem {
    private int diaDiemId;
    private String thanhPho;

    public DiaDiem(int diaDiemId, String thanhPho) {
        this.diaDiemId = diaDiemId;
        this.thanhPho = thanhPho;
    }

    public int getDiaDiemId() {
        return diaDiemId;
    }

    public void setDiaDiemId(int diaDiemId) {
        this.diaDiemId = diaDiemId;
    }

    public String getThanhPho() {
        return thanhPho;
    }

    public void setThanhPho(String thanhPho) {
        this.thanhPho = thanhPho;
    }
}
