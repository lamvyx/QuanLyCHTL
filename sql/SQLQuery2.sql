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
    [username] NVARCHAR(50) UNIQUE NOT NULL,
    [password] NVARCHAR(255) NOT NULL,
    [role] NVARCHAR(30) NOT NULL CHECK ([role] IN ('QUAN_LY', 'NHAN_VIEN')),
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
    hinhAnh NVARCHAR(255),

    FOREIGN KEY (maNCC) REFERENCES NhaCungCap(maNCC)
);

IF COL_LENGTH('SanPham', 'hinhAnh') IS NULL
BEGIN
    ALTER TABLE SanPham ADD hinhAnh NVARCHAR(255) NULL;
END;

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

-- =============================================
-- DU LIEU MAU: TAI KHOAN NHAN VIEN VA QUAN LY
-- =============================================
INSERT INTO TaiKhoan ([username], [password], [role], trangThai)
VALUES
    (N'quanly01', N'123456', N'QUAN_LY', 1),
    (N'banhang01', N'123456', N'NHAN_VIEN', 1),
    (N'banhang02', N'123456', N'NHAN_VIEN', 1);

-- =============================================
-- DU LIEU MAU BO SUNG DE DUNG THUC TE
-- =============================================
IF NOT EXISTS (SELECT 1 FROM NhaCungCap WHERE maNCC = N'NCC001')
BEGIN
    INSERT INTO NhaCungCap(maNCC, tenNCC, sdt, diaChi, email)
    VALUES (N'NCC001', N'Công ty ABC Foods', N'0281111222', N'12 Nguyễn Trãi, Q1', N'contact@abcfoods.vn');
END;

IF NOT EXISTS (SELECT 1 FROM NhaCungCap WHERE maNCC = N'NCC002')
BEGIN
    INSERT INTO NhaCungCap(maNCC, tenNCC, sdt, diaChi, email)
    VALUES (N'NCC002', N'Công ty Fresh Drink', N'0282222333', N'88 Lê Lợi, Q3', N'sales@freshdrink.vn');
END;

IF NOT EXISTS (SELECT 1 FROM NhaCungCap WHERE maNCC = N'NCC003')
BEGIN
    INSERT INTO NhaCungCap(maNCC, tenNCC, sdt, diaChi, email)
    VALUES (N'NCC003', N'Công ty Snack House', N'0283333444', N'45 Hai Bà Trưng, Q1', N'hello@snackhouse.vn');
END;

IF NOT EXISTS (SELECT 1 FROM SanPham WHERE maSP = N'SP001')
BEGIN
    INSERT INTO SanPham(maSP, tenSP, gia, hanSuDung, soLuongTon, loaiSP, maNCC, hinhAnh)
    VALUES (N'SP001', N'Bánh ngọt dâu', 25000, '2026-05-20', 40, N'Bánh ngọt', N'NCC001', N'src/images/banhngot/banh1.png');
END;

IF NOT EXISTS (SELECT 1 FROM SanPham WHERE maSP = N'SP002')
BEGIN
    INSERT INTO SanPham(maSP, tenSP, gia, hanSuDung, soLuongTon, loaiSP, maNCC, hinhAnh)
    VALUES (N'SP002', N'Khoai chiên giòn', 18000, '2026-05-10', 65, N'Đồ chiên', N'NCC003', N'src/images/dochien/dochien1.webp');
END;

IF NOT EXISTS (SELECT 1 FROM SanPham WHERE maSP = N'SP003')
BEGIN
    INSERT INTO SanPham(maSP, tenSP, gia, hanSuDung, soLuongTon, loaiSP, maNCC, hinhAnh)
    VALUES (N'SP003', N'Trà sữa kem cheese', 32000, '2026-05-18', 55, N'Thức uống', N'NCC002', N'src/images/thucuong/thucuong1.jpg');
END;

IF NOT EXISTS (SELECT 1 FROM SanPham WHERE maSP = N'SP004')
BEGIN
    INSERT INTO SanPham(maSP, tenSP, gia, hanSuDung, soLuongTon, loaiSP, maNCC, hinhAnh)
    VALUES (N'SP004', N'Mì trộn cay', 29000, '2026-05-12', 35, N'Mì', N'NCC001', N'src/images/mi/mi1.jpg');
END;

IF NOT EXISTS (SELECT 1 FROM SanPham WHERE maSP = N'SP005')
BEGIN
    INSERT INTO SanPham(maSP, tenSP, gia, hanSuDung, soLuongTon, loaiSP, maNCC, hinhAnh)
    VALUES (N'SP005', N'Sushi cá hồi', 45000, '2026-05-25', 25, N'Sushi', N'NCC001', N'src/images/sushi/su1.jpg');
END;

IF NOT EXISTS (SELECT 1 FROM SanPham WHERE maSP = N'SP006')
BEGIN
    INSERT INTO SanPham(maSP, tenSP, gia, hanSuDung, soLuongTon, loaiSP, maNCC, hinhAnh)
    VALUES (N'SP006', N'Pudding caramel', 22000, '2026-05-30', 30, N'Tráng miệng', N'NCC002', N'src/images/trangmieng/trang1.jpg');
END;

IF NOT EXISTS (SELECT 1 FROM TaiKhoan WHERE [username] = N'banhang03')
BEGIN
    INSERT INTO TaiKhoan ([username], [password], [role], trangThai)
    VALUES (N'banhang03', N'123456', N'NHAN_VIEN', 1);
END;

IF NOT EXISTS (SELECT 1 FROM TaiKhoan WHERE [username] = N'banhang04')
BEGIN
    INSERT INTO TaiKhoan ([username], [password], [role], trangThai)
    VALUES (N'banhang04', N'123456', N'NHAN_VIEN', 1);
END;

IF NOT EXISTS (SELECT 1 FROM NhanVien WHERE maNV = N'NV001')
BEGIN
    INSERT INTO NhanVien(maNV, tenNV, sdt, diaChi, ngaySinh, ngayVaoLam, maTK)
    VALUES (
        N'NV001', N'Nguyen Van Quan Ly', N'0901111222', N'12 Nguyen Trai, Q1', '1992-04-16', '2023-01-10',
        (SELECT maTK FROM TaiKhoan WHERE [username] = N'quanly01')
    );
END;

IF NOT EXISTS (SELECT 1 FROM NhanVien WHERE maNV = N'NV002')
BEGIN
    INSERT INTO NhanVien(maNV, tenNV, sdt, diaChi, ngaySinh, ngayVaoLam, maTK)
    VALUES (
        N'NV002', N'Tran Thi Ban Hang 1', N'0902222333', N'88 Le Loi, Q3', '1998-10-03', '2024-02-12',
        (SELECT maTK FROM TaiKhoan WHERE [username] = N'banhang01')
    );
END;

IF NOT EXISTS (SELECT 1 FROM NhanVien WHERE maNV = N'NV003')
BEGIN
    INSERT INTO NhanVien(maNV, tenNV, sdt, diaChi, ngaySinh, ngayVaoLam, maTK)
    VALUES (
        N'NV003', N'Le Van Ban Hang 2', N'0903333444', N'45 Hai Ba Trung, Q1', '1999-06-21', '2024-04-05',
        (SELECT maTK FROM TaiKhoan WHERE [username] = N'banhang02')
    );
END;

IF NOT EXISTS (SELECT 1 FROM NhanVien WHERE maNV = N'NV004')
BEGIN
    INSERT INTO NhanVien(maNV, tenNV, sdt, diaChi, ngaySinh, ngayVaoLam, maTK)
    VALUES (
        N'NV004', N'Pham Thi Ban Hang 3', N'0904444555', N'10 Vo Van Tan, Q3', '2000-01-15', '2025-01-08',
        (SELECT maTK FROM TaiKhoan WHERE [username] = N'banhang03')
    );
END;

IF NOT EXISTS (SELECT 1 FROM KhachHang WHERE maKH = N'KH001')
BEGIN
    INSERT INTO KhachHang(maKH, tenKH, sdt, diemTichLuy, loaiKH)
    VALUES (N'KH001', N'Hoang Minh Anh', N'0911001100', 120, N'Than thiet');
END;

IF NOT EXISTS (SELECT 1 FROM KhachHang WHERE maKH = N'KH002')
BEGIN
    INSERT INTO KhachHang(maKH, tenKH, sdt, diemTichLuy, loaiKH)
    VALUES (N'KH002', N'Nguyen Thi Uyen', N'0911002200', 45, N'Thuong');
END;

IF NOT EXISTS (SELECT 1 FROM Thue WHERE maThue = N'TH010')
BEGIN
    INSERT INTO Thue(maThue, tenThue, phanTram, moTa)
    VALUES (N'TH010', N'VAT 10%', 10, N'Thue gia tri gia tang');
END;

IF NOT EXISTS (SELECT 1 FROM KhuyenMai WHERE maKM = N'KM005')
BEGIN
    INSERT INTO KhuyenMai(maKM, tenKM, moTa, phanTramGiam, ngayBD, ngayKT)
    VALUES (N'KM005', N'Giam gia cuoi tuan', N'Ap dung cho hoa don tu 200k', 5, '2026-01-01', '2026-12-31');
END;

IF NOT EXISTS (SELECT 1 FROM HoaDon WHERE maHD = N'HD001')
BEGIN
    INSERT INTO HoaDon(maHD, ngayLap, maNV, maKH, maThue, maKM)
    VALUES (N'HD001', '2026-04-01', N'NV002', N'KH001', N'TH010', N'KM005');
END;

IF NOT EXISTS (SELECT 1 FROM HoaDon WHERE maHD = N'HD002')
BEGIN
    INSERT INTO HoaDon(maHD, ngayLap, maNV, maKH, maThue, maKM)
    VALUES (N'HD002', '2026-04-10', N'NV003', N'KH002', N'TH010', NULL);
END;

IF NOT EXISTS (SELECT 1 FROM HoaDon WHERE maHD = N'HD003')
BEGIN
    INSERT INTO HoaDon(maHD, ngayLap, maNV, maKH, maThue, maKM)
    VALUES (N'HD003', '2026-04-22', N'NV004', NULL, N'TH010', NULL);
END;