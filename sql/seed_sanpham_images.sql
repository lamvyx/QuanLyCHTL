USE QLCuaHang;
GO

-- Seed sample products using images from src/images

IF NOT EXISTS (SELECT 1 FROM SanPham WHERE maSP = N'SP007')
BEGIN
    INSERT INTO SanPham(maSP, tenSP, gia, hanSuDung, soLuongTon, loaiSP, maNCC, hinhAnh)
    VALUES (N'SP007', N'Bánh ngọt mix', 27000, '2026-06-01', 30, N'Bánh ngọt', N'NCC001', N'src/images/banhngot/banh2.jpg');
END;

IF NOT EXISTS (SELECT 1 FROM SanPham WHERE maSP = N'SP008')
BEGIN
    INSERT INTO SanPham(maSP, tenSP, gia, hanSuDung, soLuongTon, loaiSP, maNCC, hinhAnh)
    VALUES (N'SP008', N'Khoai chiên sốt', 19000, '2026-06-10', 50, N'Đồ chiên', N'NCC001', N'src/images/dochien/dochien2.jpg');
END;

IF NOT EXISTS (SELECT 1 FROM SanPham WHERE maSP = N'SP009')
BEGIN
    INSERT INTO SanPham(maSP, tenSP, gia, hanSuDung, soLuongTon, loaiSP, maNCC, hinhAnh)
    VALUES (N'SP009', N'Trá sữa xoài', 33000, '2026-06-05', 45, N'Thức uống', N'NCC002', N'src/images/thucuong/thucuong3.jpg');
END;

IF NOT EXISTS (SELECT 1 FROM SanPham WHERE maSP = N'SP010')
BEGIN
    INSERT INTO SanPham(maSP, tenSP, gia, hanSuDung, soLuongTon, loaiSP, maNCC, hinhAnh)
    VALUES (N'SP010', N'Mì trộn đặc biệt', 31000, '2026-06-12', 25, N'Mì', N'NCC001', N'src/images/mi/mi4.png');
END;

IF NOT EXISTS (SELECT 1 FROM SanPham WHERE maSP = N'SP011')
BEGIN
    INSERT INTO SanPham(maSP, tenSP, gia, hanSuDung, soLuongTon, loaiSP, maNCC, hinhAnh)
    VALUES (N'SP011', N'Sushi set nhỏ', 48000, '2026-06-20', 20, N'Sushi', N'NCC002', N'src/images/sushi/su3.png');
END;

IF NOT EXISTS (SELECT 1 FROM SanPham WHERE maSP = N'SP012')
BEGIN
    INSERT INTO SanPham(maSP, tenSP, gia, hanSuDung, soLuongTon, loaiSP, maNCC, hinhAnh)
    VALUES (N'SP012', N'Bánh mì xúc xích', 22000, '2026-06-15', 40, N'Bánh mì', N'NCC003', N'src/images/banhmi.png');
END;

GO
