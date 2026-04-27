package entity;

import java.time.LocalDate;

public class KhuyenMai {
    private String maKM;
    private String tenKM;
    private String moTa;
    private Double phanTramGiam;
    private LocalDate ngayBD;
    private LocalDate ngayKT;

    public KhuyenMai() {
    }

    public KhuyenMai(String maKM, String tenKM, String moTa, Double phanTramGiam, LocalDate ngayBD, LocalDate ngayKT) {
        this.maKM = maKM;
        this.tenKM = tenKM;
        this.moTa = moTa;
        this.phanTramGiam = phanTramGiam;
        this.ngayBD = ngayBD;
        this.ngayKT = ngayKT;
    }

    public String getMaKM() {
        return maKM;
    }

    public void setMaKM(String maKM) {
        this.maKM = maKM;
    }

    public String getTenKM() {
        return tenKM;
    }

    public void setTenKM(String tenKM) {
        this.tenKM = tenKM;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public Double getPhanTramGiam() {
        return phanTramGiam;
    }

    public void setPhanTramGiam(Double phanTramGiam) {
        this.phanTramGiam = phanTramGiam;
    }

    public LocalDate getNgayBD() {
        return ngayBD;
    }

    public void setNgayBD(LocalDate ngayBD) {
        this.ngayBD = ngayBD;
    }

    public LocalDate getNgayKT() {
        return ngayKT;
    }

    public void setNgayKT(LocalDate ngayKT) {
        this.ngayKT = ngayKT;
    }

    @Override
    public String toString() {
        return "KhuyenMai{" +
                "maKM='" + maKM + '\'' +
                ", tenKM='" + tenKM + '\'' +
                ", phanTramGiam=" + phanTramGiam +
                '}';
    }
}
