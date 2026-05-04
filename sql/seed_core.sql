USE QLCuaHang;
GO

-- Seed core data: KhachHang, Thue, KhuyenMai, NhanVien, TaiKhoan

-- 1. TaiKhoan & NhanVien
IF NOT EXISTS (SELECT 1 FROM TaiKhoan WHERE [username] = N'admin')
BEGIN
    INSERT INTO TaiKhoan ([username], [password], [role], trangThai)
    VALUES (N'admin', N'admin', N'QUAN_LY', 1);
END;

IF NOT EXISTS (SELECT 1 FROM NhanVien WHERE maNV = N'NV001')
BEGIN
    INSERT INTO NhanVien(maNV, tenNV, sdt, diaChi, ngaySinh, ngayVaoLam, maTK)
    VALUES (N'NV001', N'Nguyễn Quản Lý', N'0901234567', N'Quận 1, TP.HCM', '1990-01-01', '2023-01-01', 
           (SELECT maTK FROM TaiKhoan WHERE [username] = N'admin'));
END;

IF NOT EXISTS (SELECT 1 FROM TaiKhoan WHERE [username] = N'staff')
BEGIN
    INSERT INTO TaiKhoan ([username], [password], [role], trangThai)
    VALUES (N'staff', N'123', N'NHAN_VIEN', 1);
END;

IF NOT EXISTS (SELECT 1 FROM NhanVien WHERE maNV = N'NV002')
BEGIN
    INSERT INTO NhanVien(maNV, tenNV, sdt, diaChi, ngaySinh, ngayVaoLam, maTK)
    VALUES (N'NV002', N'Trần Bán Hàng', N'0907654321', N'Quận 3, TP.HCM', '1995-05-05', '2024-01-01', 
           (SELECT maTK FROM TaiKhoan WHERE [username] = N'staff'));
END;

-- 2. KhachHang
IF NOT EXISTS (SELECT 1 FROM KhachHang WHERE maKH = N'KH001')
    INSERT INTO KhachHang(maKH, tenKH, sdt, diemTichLuy, loaiKH) VALUES (N'KH001', N'Nguyễn Văn An', N'0912345678', 100, N'Thân thiết');
IF NOT EXISTS (SELECT 1 FROM KhachHang WHERE maKH = N'KH002')
    INSERT INTO KhachHang(maKH, tenKH, sdt, diemTichLuy, loaiKH) VALUES (N'KH002', N'Lê Thị Bình', N'0918765432', 50, N'Thường');
IF NOT EXISTS (SELECT 1 FROM KhachHang WHERE maKH = N'KH003')
    INSERT INTO KhachHang(maKH, tenKH, sdt, diemTichLuy, loaiKH) VALUES (N'KH003', N'Phạm Hồng Phúc', N'0911223344', 200, N'VIP');

-- 3. Thue
IF NOT EXISTS (SELECT 1 FROM Thue WHERE maThue = N'VAT10')
    INSERT INTO Thue(maThue, tenThue, phanTram, moTa) VALUES (N'VAT10', N'VAT 10%', 10, N'Thuế GTGT 10%');
IF NOT EXISTS (SELECT 1 FROM Thue WHERE maThue = N'VAT08')
    INSERT INTO Thue(maThue, tenThue, phanTram, moTa) VALUES (N'VAT08', N'VAT 8%', 8, N'Thuế GTGT 8%');

-- 4. KhuyenMai
IF NOT EXISTS (SELECT 1 FROM KhuyenMai WHERE maKM = N'KM001')
    INSERT INTO KhuyenMai(maKM, tenKM, phanTramGiam, ngayBD, ngayKT, moTa) 
    VALUES (N'KM001', N'Chào hè 2026', 10, '2026-05-01', '2026-08-31', N'Giảm 10% toàn hóa đơn');
IF NOT EXISTS (SELECT 1 FROM KhuyenMai WHERE maKM = N'KM002')
    INSERT INTO KhuyenMai(maKM, tenKM, phanTramGiam, ngayBD, ngayKT, moTa) 
    VALUES (N'KM002', N'Khai trương chi nhánh', 20, '2026-01-01', '2026-01-31', N'Giảm 20% tri ân khách hàng');

GO
