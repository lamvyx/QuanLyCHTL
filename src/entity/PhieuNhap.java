package entity;

import java.time.LocalDate;

public class PhieuNhap {
    private String maPhieu;
    private LocalDate ngayNhap;
    private String ghiChu;
    private String maNV;
    private String maNCC;

    public PhieuNhap() {
    }

    public PhieuNhap(String maPhieu, LocalDate ngayNhap, String ghiChu, String maNV, String maNCC) {
        this.maPhieu = maPhieu;
        this.ngayNhap = ngayNhap;
        this.ghiChu = ghiChu;
        this.maNV = maNV;
        this.maNCC = maNCC;
    }

    public String getMaPhieu() {
        return maPhieu;
    }

    public void setMaPhieu(String maPhieu) {
        this.maPhieu = maPhieu;
    }

    public LocalDate getNgayNhap() {
        return ngayNhap;
    }

    public void setNgayNhap(LocalDate ngayNhap) {
        this.ngayNhap = ngayNhap;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public String getMaNCC() {
        return maNCC;
    }

    public void setMaNCC(String maNCC) {
        this.maNCC = maNCC;
    }

    @Override
    public String toString() {
        return "PhieuNhap{" +
                "maPhieu='" + maPhieu + '\'' +
                ", ngayNhap=" + ngayNhap +
                ", maNV='" + maNV + '\'' +
                ", maNCC='" + maNCC + '\'' +
                '}';
    }
}
