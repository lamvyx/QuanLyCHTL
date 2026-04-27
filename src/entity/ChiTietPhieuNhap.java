package entity;

public class ChiTietPhieuNhap {
    private String maPhieu;
    private String maSP;
    private Integer soLuong;
    private Double giaNhap;

    public ChiTietPhieuNhap() {
    }

    public ChiTietPhieuNhap(String maPhieu, String maSP, Integer soLuong, Double giaNhap) {
        this.maPhieu = maPhieu;
        this.maSP = maSP;
        this.soLuong = soLuong;
        this.giaNhap = giaNhap;
    }

    public String getMaPhieu() {
        return maPhieu;
    }

    public void setMaPhieu(String maPhieu) {
        this.maPhieu = maPhieu;
    }

    public String getMaSP() {
        return maSP;
    }

    public void setMaSP(String maSP) {
        this.maSP = maSP;
    }

    public Integer getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(Integer soLuong) {
        this.soLuong = soLuong;
    }

    public Double getGiaNhap() {
        return giaNhap;
    }

    public void setGiaNhap(Double giaNhap) {
        this.giaNhap = giaNhap;
    }

    @Override
    public String toString() {
        return "ChiTietPhieuNhap{" +
                "maPhieu='" + maPhieu + '\'' +
                ", maSP='" + maSP + '\'' +
                ", soLuong=" + soLuong +
                ", giaNhap=" + giaNhap +
                '}';
    }
}
