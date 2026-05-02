package entity;

import java.time.LocalDate;

public class SanPham {
    private String maSP;
    private String tenSP;
    private Double gia;
    private LocalDate hanSuDung;
    private Integer soLuongTon;
    private String loaiSP;
    private String maNCC;
    private String hinhAnh;

    public SanPham() {
    }

    public SanPham(String maSP, String tenSP, Double gia, LocalDate hanSuDung, Integer soLuongTon, String loaiSP, String maNCC, String hinhAnh) {
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.gia = gia;
        this.hanSuDung = hanSuDung;
        this.soLuongTon = soLuongTon;
        this.loaiSP = loaiSP;
        this.maNCC = maNCC;
        this.hinhAnh = hinhAnh;
    }

    public String getMaSP() {
        return maSP;
    }

    public void setMaSP(String maSP) {
        this.maSP = maSP;
    }

    public String getTenSP() {
        return tenSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    public Double getGia() {
        return gia;
    }

    public void setGia(Double gia) {
        this.gia = gia;
    }

    public LocalDate getHanSuDung() {
        return hanSuDung;
    }

    public void setHanSuDung(LocalDate hanSuDung) {
        this.hanSuDung = hanSuDung;
    }

    public Integer getSoLuongTon() {
        return soLuongTon;
    }

    public void setSoLuongTon(Integer soLuongTon) {
        this.soLuongTon = soLuongTon;
    }

    public String getLoaiSP() {
        return loaiSP;
    }

    public void setLoaiSP(String loaiSP) {
        this.loaiSP = loaiSP;
    }

    public String getMaNCC() {
        return maNCC;
    }

    public void setMaNCC(String maNCC) {
        this.maNCC = maNCC;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    @Override
    public String toString() {
        return "SanPham{" +
                "maSP='" + maSP + '\'' +
                ", tenSP='" + tenSP + '\'' +
                ", gia=" + gia +
                ", soLuongTon=" + soLuongTon +
                ", hinhAnh='" + hinhAnh + '\'' +
                '}';
    }
}
