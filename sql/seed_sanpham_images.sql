USE QLCuaHang;
GO

-- Seed Sản phẩm phân loại theo thư mục ảnh

-- 1. Bánh hấp (banhhap)
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE maSP = N'SP001')
    INSERT INTO SanPham(maSP, tenSP, gia, hanSuDung, soLuongTon, loaiSP, maNCC, hinhAnh)
    VALUES (N'SP001', N'Bánh bao xá xíu', 22000, '2026-06-10', 50, N'Bánh hấp', N'NCC001', N'src/images/banhhap/banhhap1.jpg');
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE maSP = N'SP002')
    INSERT INTO SanPham(maSP, tenSP, gia, hanSuDung, soLuongTon, loaiSP, maNCC, hinhAnh)
    VALUES (N'SP002', N'Bánh bao kim sa', 25000, '2026-06-12', 40, N'Bánh hấp', N'NCC001', N'src/images/banhhap/banhhap2.jpg');

-- 2. Bánh ngọt (banhngot)
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE maSP = N'SP003')
    INSERT INTO SanPham(maSP, tenSP, gia, hanSuDung, soLuongTon, loaiSP, maNCC, hinhAnh)
    VALUES (N'SP003', N'Croissant bơ', 18000, '2026-06-15', 60, N'Bánh ngọt', N'NCC002', N'src/images/banhngot/banh1.png');
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE maSP = N'SP004')
    INSERT INTO SanPham(maSP, tenSP, gia, hanSuDung, soLuongTon, loaiSP, maNCC, hinhAnh)
    VALUES (N'SP004', N'Muffin socola', 20000, '2026-06-15', 55, N'Bánh ngọt', N'NCC002', N'src/images/banhngot/banh2.jpg');

-- 3. Đồ chiên (dochien)
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE maSP = N'SP005')
    INSERT INTO SanPham(maSP, tenSP, gia, hanSuDung, soLuongTon, loaiSP, maNCC, hinhAnh)
    VALUES (N'SP005', N'Gà rán giòn rụm', 35000, '2026-06-20', 30, N'Đồ chiên', N'NCC003', N'src/images/dochien/dochien1.webp');
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE maSP = N'SP006')
    INSERT INTO SanPham(maSP, tenSP, gia, hanSuDung, soLuongTon, loaiSP, maNCC, hinhAnh)
    VALUES (N'SP006', N'Khoai tây chiên', 25000, '2026-06-20', 45, N'Đồ chiên', N'NCC003', N'src/images/dochien/dochien2.jpg');

-- 4. Hàng tiêu dùng (hangtieudung)
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE maSP = N'SP007')
    INSERT INTO SanPham(maSP, tenSP, gia, hanSuDung, soLuongTon, loaiSP, maNCC, hinhAnh)
    VALUES (N'SP007', N'Khăn giấy bỏ túi', 5000, '2030-01-01', 100, N'Hàng tiêu dùng', N'NCC004', N'src/images/hangtieudung/hang1.jpg');
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE maSP = N'SP008')
    INSERT INTO SanPham(maSP, tenSP, gia, hanSuDung, soLuongTon, loaiSP, maNCC, hinhAnh)
    VALUES (N'SP008', N'Bàn chải đánh răng', 15000, '2030-01-01', 80, N'Hàng tiêu dùng', N'NCC004', N'src/images/hangtieudung/hang2.jpg');

-- 5. Lẩu (lau)
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE maSP = N'SP009')
    INSERT INTO SanPham(maSP, tenSP, gia, hanSuDung, soLuongTon, loaiSP, maNCC, hinhAnh)
    VALUES (N'SP009', N'Lẩu Thái mini', 55000, '2026-06-05', 20, N'Lẩu', N'NCC005', N'src/images/lau/lau1.jpg');
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE maSP = N'SP010')
    INSERT INTO SanPham(maSP, tenSP, gia, hanSuDung, soLuongTon, loaiSP, maNCC, hinhAnh)
    VALUES (N'SP010', N'Lẩu bò sa tế', 59000, '2026-06-05', 15, N'Lẩu', N'NCC005', N'src/images/lau/lau2.jpg');

-- 6. Mì (mi)
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE maSP = N'SP011')
    INSERT INTO SanPham(maSP, tenSP, gia, hanSuDung, soLuongTon, loaiSP, maNCC, hinhAnh)
    VALUES (N'SP011', N'Mì Ý bò bằm', 45000, '2026-06-08', 25, N'Mì', N'NCC006', N'src/images/mi/mi1.jpg');
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE maSP = N'SP012')
    INSERT INTO SanPham(maSP, tenSP, gia, hanSuDung, soLuongTon, loaiSP, maNCC, hinhAnh)
    VALUES (N'SP012', N'Mì Ramen trứng', 49000, '2026-06-08', 30, N'Mì', N'NCC006', N'src/images/mi/mi2.jpg');

-- 7. Omusubi (omusubi)
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE maSP = N'SP013')
    INSERT INTO SanPham(maSP, tenSP, gia, hanSuDung, soLuongTon, loaiSP, maNCC, hinhAnh)
    VALUES (N'SP013', N'Cơm nắm cá ngừ', 15000, '2026-06-02', 40, N'Omusubi', N'NCC002', N'src/images/omusubi/omu1.jpg');
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE maSP = N'SP014')
    INSERT INTO SanPham(maSP, tenSP, gia, hanSuDung, soLuongTon, loaiSP, maNCC, hinhAnh)
    VALUES (N'SP014', N'Cơm nắm xá xíu', 16000, '2026-06-02', 35, N'Omusubi', N'NCC002', N'src/images/omusubi/omu2.jpg');

-- 8. Sandwich (sandwich)
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE maSP = N'SP015')
    INSERT INTO SanPham(maSP, tenSP, gia, hanSuDung, soLuongTon, loaiSP, maNCC, hinhAnh)
    VALUES (N'SP015', N'Sandwich trứng Mayo', 28000, '2026-06-03', 40, N'Sandwich', N'NCC001', N'src/images/sandwich/sand1.jpg');
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE maSP = N'SP016')
    INSERT INTO SanPham(maSP, tenSP, gia, hanSuDung, soLuongTon, loaiSP, maNCC, hinhAnh)
    VALUES (N'SP016', N'Sandwich thịt nguội', 32000, '2026-06-03', 30, N'Sandwich', N'NCC001', N'src/images/sandwich/sand2.jpg');

-- 9. Sushi (sushi)
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE maSP = N'SP017')
    INSERT INTO SanPham(maSP, tenSP, gia, hanSuDung, soLuongTon, loaiSP, maNCC, hinhAnh)
    VALUES (N'SP017', N'Sushi lươn Nhật', 45000, '2026-06-01', 20, N'Sushi', N'NCC007', N'src/images/sushi/su1.jpg');
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE maSP = N'SP018')
    INSERT INTO SanPham(maSP, tenSP, gia, hanSuDung, soLuongTon, loaiSP, maNCC, hinhAnh)
    VALUES (N'SP018', N'Sushi tôm tươi', 38000, '2026-06-01', 25, N'Sushi', N'NCC007', N'src/images/sushi/su2.jpg');

-- 10. Thức uống (thucuong)
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE maSP = N'SP019')
    INSERT INTO SanPham(maSP, tenSP, gia, hanSuDung, soLuongTon, loaiSP, maNCC, hinhAnh)
    VALUES (N'SP019', N'Cà phê sữa đá', 25000, '2026-06-30', 100, N'Thức uống', N'NCC002', N'src/images/thucuong/thucuong1.jpg');
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE maSP = N'SP020')
    INSERT INTO SanPham(maSP, tenSP, gia, hanSuDung, soLuongTon, loaiSP, maNCC, hinhAnh)
    VALUES (N'SP020', N'Trà chanh sả', 20000, '2026-06-30', 120, N'Thức uống', N'NCC002', N'src/images/thucuong/thucuong2.jpg');

-- 11. Tráng miệng (trangmieng)
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE maSP = N'SP021')
    INSERT INTO SanPham(maSP, tenSP, gia, hanSuDung, soLuongTon, loaiSP, maNCC, hinhAnh)
    VALUES (N'SP021', N'Rau câu dừa', 12000, '2026-06-15', 50, N'Tráng miệng', N'NCC008', N'src/images/trangmieng/trang1.jpg');
IF NOT EXISTS (SELECT 1 FROM SanPham WHERE maSP = N'SP022')
    INSERT INTO SanPham(maSP, tenSP, gia, hanSuDung, soLuongTon, loaiSP, maNCC, hinhAnh)
    VALUES (N'SP022', N'Chè dưỡng nhan', 35000, '2026-06-15', 30, N'Tráng miệng', N'NCC008', N'src/images/trangmieng/trang2.jpg');

GO
