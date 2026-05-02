USE QLCuaHang;
GO

IF NOT EXISTS (SELECT 1 FROM TaiKhoan WHERE [username] = N'quanly01')
BEGIN
    INSERT INTO TaiKhoan ([username], [password], [role], trangThai)
    VALUES (N'quanly01', N'123456', N'QUAN_LY', 1);
END;

IF NOT EXISTS (SELECT 1 FROM TaiKhoan WHERE [username] = N'banhang01')
BEGIN
    INSERT INTO TaiKhoan ([username], [password], [role], trangThai)
    VALUES (N'banhang01', N'123456', N'NHAN_VIEN', 1);
END;

IF NOT EXISTS (SELECT 1 FROM TaiKhoan WHERE [username] = N'banhang02')
BEGIN
    INSERT INTO TaiKhoan ([username], [password], [role], trangThai)
    VALUES (N'banhang02', N'123456', N'NHAN_VIEN', 1);
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
GO
