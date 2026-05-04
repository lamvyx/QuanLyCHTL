package entity;

public class Thue {
    private String maThue;
    private String tenThue;
    private Double phanTram;
    private String moTa;

    public Thue() {
    }

    public Thue(String maThue, String tenThue, Double phanTram, String moTa) {
        this.maThue = maThue;
        this.tenThue = tenThue;
        this.phanTram = phanTram;
        this.moTa = moTa;
    }

    public String getMaThue() {
        return maThue;
    }

    public void setMaThue(String maThue) {
        this.maThue = maThue;
    }

    public String getTenThue() {
        return tenThue;
    }

    public void setTenThue(String tenThue) {
        this.tenThue = tenThue;
    }

    public Double getPhanTram() {
        return phanTram;
    }

    public void setPhanTram(Double phanTram) {
        this.phanTram = phanTram;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    @Override
    public String toString() {
        return "Thue{" +
                "maThue='" + maThue + '\'' +
                ", tenThue='" + tenThue + '\'' +
                ", phanTram=" + phanTram +
                '}';
    }
}
