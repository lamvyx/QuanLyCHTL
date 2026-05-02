USE QLCuaHang;
GO

-- Thêm một số nhà cung cấp mẫu

IF NOT EXISTS (SELECT 1 FROM NhaCungCap WHERE maNCC = N'NCC004')
BEGIN
    INSERT INTO NhaCungCap(maNCC, tenNCC, sdt, diaChi, email)
    VALUES (N'NCC004', N'Gia Vinh Foods', N'0284444555', N'45 Tran Hung Dao, Q1', N'contact@giavinh.vn');
END;

IF NOT EXISTS (SELECT 1 FROM NhaCungCap WHERE maNCC = N'NCC005')
BEGIN
    INSERT INTO NhaCungCap(maNCC, tenNCC, sdt, diaChi, email)
    VALUES (N'NCC005', N'Fresh Supplies Co.', N'0285555666', N'123 Le Loi, Q3', N'sales@freshsupplies.vn');
END;

IF NOT EXISTS (SELECT 1 FROM NhaCungCap WHERE maNCC = N'NCC006')
BEGIN
    INSERT INTO NhaCungCap(maNCC, tenNCC, sdt, diaChi, email)
    VALUES (N'NCC006', N'Sunrise Ingredients', N'0286666777', N'78 Nguyen Trai, Q5', N'hello@sunrise.vn');
END;

IF NOT EXISTS (SELECT 1 FROM NhaCungCap WHERE maNCC = N'NCC007')
BEGIN
    INSERT INTO NhaCungCap(maNCC, tenNCC, sdt, diaChi, email)
    VALUES (N'NCC007', N'Oceanic Beverages', N'0287777888', N'10 Hai Ba Trung, Q1', N'info@oceanicbev.vn');
END;

IF NOT EXISTS (SELECT 1 FROM NhaCungCap WHERE maNCC = N'NCC008')
BEGIN
    INSERT INTO NhaCungCap(maNCC, tenNCC, sdt, diaChi, email)
    VALUES (N'NCC008', N'Snack House Ltd.', N'0288888999', N'200 Vo Van Kiet, Q6', N'contact@snackhouse.vn');
END;

GO
