USE QLCuaHang;
GO

-- 1. NhaCungCap (20 items)
DECLARE @i1 INT = 11;
WHILE @i1 <= 30
BEGIN
    DECLARE @maNCC NVARCHAR(10) = 'NCC' + RIGHT('000' + CAST(@i1 AS NVARCHAR), 3);
    IF NOT EXISTS (SELECT 1 FROM NhaCungCap WHERE maNCC = @maNCC)
        INSERT INTO NhaCungCap(maNCC, tenNCC, sdt, diaChi, email)
        VALUES (@maNCC, N'Nhà cung cấp ' + CAST(@i1 AS NVARCHAR), '0900000' + RIGHT('00' + CAST(@i1 AS NVARCHAR), 3), N'Địa chỉ ' + CAST(@i1 AS NVARCHAR), 'ncc' + CAST(@i1 AS NVARCHAR) + '@gmail.com');
    SET @i1 = @i1 + 1;
END

-- 2. KhachHang (20 items)
DECLARE @i2 INT = 4; -- Start from KH004
WHILE @i2 <= 23
BEGIN
    DECLARE @maKH NVARCHAR(10) = 'KH' + RIGHT('000' + CAST(@i2 AS NVARCHAR), 3);
    IF NOT EXISTS (SELECT 1 FROM KhachHang WHERE maKH = @maKH)
        INSERT INTO KhachHang(maKH, tenKH, sdt, diemTichLuy, loaiKH)
        VALUES (@maKH, N'Khách hàng ' + CAST(@i2 AS NVARCHAR), '0800000' + RIGHT('00' + CAST(@i2 AS NVARCHAR), 3), 100 * @i2, N'Thường');
    SET @i2 = @i2 + 1;
END

-- 3. NhanVien & TaiKhoan (20 items)
DECLARE @i3 INT = 10;
WHILE @i3 <= 29
BEGIN
    DECLARE @maNV NVARCHAR(10) = 'NV' + RIGHT('000' + CAST(@i3 AS NVARCHAR), 3);
    DECLARE @user NVARCHAR(50) = 'nv' + CAST(@i3 AS NVARCHAR);
    IF NOT EXISTS (SELECT 1 FROM NhanVien WHERE maNV = @maNV)
    BEGIN
        IF NOT EXISTS (SELECT 1 FROM TaiKhoan WHERE username = @user)
        BEGIN
            INSERT INTO TaiKhoan(username, [password], [role], trangThai)
            VALUES (@user, '123456', 'NHAN_VIEN', 1);
            DECLARE @maTK INT = SCOPE_IDENTITY();
            INSERT INTO NhanVien(maNV, tenNV, sdt, diaChi, ngaySinh, ngayVaoLam, maTK)
            VALUES (@maNV, N'Nhân viên ' + CAST(@i3 AS NVARCHAR), '0700000' + RIGHT('00' + CAST(@i3 AS NVARCHAR), 3), N'Địa chỉ NV ' + CAST(@i3 AS NVARCHAR), '1995-01-01', GETDATE(), @maTK);
        END
    END
    SET @i3 = @i3 + 1;
END

-- 4. SanPham (20 items)
DECLARE @i4 INT = 23;
WHILE @i4 <= 42
BEGIN
    DECLARE @maSP NVARCHAR(10) = 'SP' + RIGHT('000' + CAST(@i4 AS NVARCHAR), 3);
    IF NOT EXISTS (SELECT 1 FROM SanPham WHERE maSP = @maSP)
        INSERT INTO SanPham(maSP, tenSP, gia, hanSuDung, soLuongTon, loaiSP, maNCC, hinhAnh)
        VALUES (@maSP, N'Sản phẩm mới ' + CAST(@i4 AS NVARCHAR), 10000 * @i4, '2026-12-31', 100, N'Hàng tổng hợp', N'NCC001', NULL);
    SET @i4 = @i4 + 1;
END

-- 5. HoaDon & ChiTietHoaDon (20 items)
DECLARE @i5 INT = 1;
WHILE @i5 <= 20
BEGIN
    DECLARE @maHD NVARCHAR(10) = 'HD' + CAST(100 + @i5 AS NVARCHAR);
    IF NOT EXISTS (SELECT 1 FROM HoaDon WHERE maHD = @maHD)
    BEGIN
        INSERT INTO HoaDon(maHD, ngayLap, maNV, maKH, maThue, maKM)
        VALUES (@maHD, GETDATE(), 'NV001', 'KH001', NULL, NULL);
        INSERT INTO ChiTietHoaDon(maHD, maSP, soLuong, donGia)
        VALUES (@maHD, 'SP001', 1, 22000);
    END
    SET @i5 = @i5 + 1;
END

-- 6. PhieuNhap & ChiTietPhieuNhap (20 items)
DECLARE @i6 INT = 1;
WHILE @i6 <= 20
BEGIN
    DECLARE @maPN NVARCHAR(10) = 'PN' + CAST(100 + @i6 AS NVARCHAR);
    IF NOT EXISTS (SELECT 1 FROM PhieuNhap WHERE maPhieu = @maPN)
    BEGIN
        INSERT INTO PhieuNhap(maPhieu, ngayNhap, maNV, maNCC, ghiChu)
        VALUES (@maPN, GETDATE(), 'NV001', 'NCC001', N'Nhập mẫu ' + CAST(@i6 AS NVARCHAR));
        INSERT INTO ChiTietPhieuNhap(maPhieu, maSP, soLuong, giaNhap)
        VALUES (@maPN, 'SP001', 10, 15000);
    END
    SET @i6 = @i6 + 1;
END
