-- =============================================
-- DATABASE
-- =============================================
IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = 'QLCuaHang')
    CREATE DATABASE QLCuaHang;
GO

USE QLCuaHang;
GO

-- =============================================
-- TAI KHOAN
-- =============================================
CREATE TABLE TaiKhoan (
    maTK INT PRIMARY KEY IDENTITY(1,1),
    username NVARCHAR(50) UNIQUE NOT NULL,
    password NVARCHAR(255) NOT NULL,
    role NVARCHAR(30) NOT NULL CHECK (role IN ('QUAN_LY', 'NHAN_VIEN')),
    trangThai BIT NOT NULL DEFAULT 1
);

-- =============================================
-- NHAN VIEN
-- =============================================
CREATE TABLE NhanVien (
    maNV NVARCHAR(10) PRIMARY KEY,
    tenNV NVARCHAR(100) NOT NULL,
    sdt NVARCHAR(15),
    diaChi NVARCHAR(255),
    ngaySinh DATE,
    ngayVaoLam DATE,
    maTK INT UNIQUE,
    FOREIGN KEY (maTK) REFERENCES TaiKhoan(maTK)
);

-- =============================================
-- KHACH HANG
-- =============================================
CREATE TABLE KhachHang (
    maKH NVARCHAR(10) PRIMARY KEY,
    tenKH NVARCHAR(100) NOT NULL,
    sdt NVARCHAR(15),
    diemTichLuy INT DEFAULT 0,
    loaiKH NVARCHAR(50)
);

-- =============================================
-- THUE
-- =============================================
CREATE TABLE Thue (
    maThue NVARCHAR(10) PRIMARY KEY,
    tenThue NVARCHAR(100),
    phanTram FLOAT CHECK (phanTram >= 0 AND phanTram <= 100),
    moTa NVARCHAR(255)
);

-- =============================================
-- KHUYEN MAI (áp dụng cho HÓA ĐƠN)
-- =============================================
CREATE TABLE KhuyenMai (
    maKM NVARCHAR(10) PRIMARY KEY,
    tenKM NVARCHAR(100),
    moTa NVARCHAR(255),
    phanTramGiam FLOAT CHECK (phanTramGiam >= 0 AND phanTramGiam <= 100),
    ngayBD DATE,
    ngayKT DATE
);

-- =============================================
-- HOA DON
-- =============================================
CREATE TABLE HoaDon (
    maHD NVARCHAR(10) PRIMARY KEY,
    ngayLap DATE DEFAULT GETDATE(),
    maNV NVARCHAR(10),
    maKH NVARCHAR(10),
    maThue NVARCHAR(10),
    maKM NVARCHAR(10),

    FOREIGN KEY (maNV) REFERENCES NhanVien(maNV),
    FOREIGN KEY (maKH) REFERENCES KhachHang(maKH),
    FOREIGN KEY (maThue) REFERENCES Thue(maThue),
    FOREIGN KEY (maKM) REFERENCES KhuyenMai(maKM)
);

-- =============================================
-- SAN PHAM
-- =============================================
CREATE TABLE SanPham (
    maSP NVARCHAR(10) PRIMARY KEY,
    tenSP NVARCHAR(150),
    gia FLOAT CHECK (gia >= 0),
    hanSuDung DATE,
    soLuongTon INT DEFAULT 0,
    loaiSP NVARCHAR(100),
    maNCC NVARCHAR(10),

    FOREIGN KEY (maNCC) REFERENCES NhaCungCap(maNCC)
);

-- =============================================
-- CHI TIET HOA DON
-- =============================================
CREATE TABLE ChiTietHoaDon (
    maHD NVARCHAR(10),
    maSP NVARCHAR(10),
    soLuong INT CHECK (soLuong > 0),
    donGia FLOAT CHECK (donGia >= 0),

    PRIMARY KEY (maHD, maSP),
    FOREIGN KEY (maHD) REFERENCES HoaDon(maHD) ON DELETE CASCADE,
    FOREIGN KEY (maSP) REFERENCES SanPham(maSP)
);

-- =============================================
-- NHA CUNG CAP
-- =============================================
CREATE TABLE NhaCungCap (
    maNCC NVARCHAR(10) PRIMARY KEY,
    tenNCC NVARCHAR(100),
    sdt NVARCHAR(15),
    diaChi NVARCHAR(255),
    email NVARCHAR(100)
);

-- =============================================
-- PHIEU NHAP
-- =============================================
CREATE TABLE PhieuNhap (
    maPhieu NVARCHAR(10) PRIMARY KEY,
    ngayNhap DATE DEFAULT GETDATE(),
    ghiChu NVARCHAR(255),
    maNV NVARCHAR(10),
    maNCC NVARCHAR(10),

    FOREIGN KEY (maNV) REFERENCES NhanVien(maNV),
    FOREIGN KEY (maNCC) REFERENCES NhaCungCap(maNCC)
);

-- =============================================
-- CHI TIET PHIEU NHAP
-- =============================================
CREATE TABLE ChiTietPhieuNhap (
    maPhieu NVARCHAR(10),
    maSP NVARCHAR(10),
    soLuong INT CHECK (soLuong > 0),
    giaNhap FLOAT CHECK (giaNhap >= 0),

    PRIMARY KEY (maPhieu, maSP),
    FOREIGN KEY (maPhieu) REFERENCES PhieuNhap(maPhieu) ON DELETE CASCADE,
    FOREIGN KEY (maSP) REFERENCES SanPham(maSP)
);