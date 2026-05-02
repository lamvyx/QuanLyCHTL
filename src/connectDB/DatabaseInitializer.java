package connectDB;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    public static void ensureSchema() {
        String sql = "IF COL_LENGTH('SanPham', 'hinhAnh') IS NULL BEGIN ALTER TABLE SanPham ADD hinhAnh NVARCHAR(255) NULL; END";
        try (Connection con = ConnectDB.getInstance().getConnection();
             Statement st = con.createStatement()) {
            st.execute(sql);
        } catch (SQLException e) {
            throw new IllegalStateException("Lỗi khi đảm bảo schema: " + e.getMessage(), e);
        }
    }

    public static void loadSeedData() {
        String[] seedFiles = {
                "sql/seed_nhacungcap.sql",
                "sql/seed_sanpham_images.sql"
        };
        for (String file : seedFiles) {
            try {
                SQLSeedLoader.loadSqlFile(file);
                System.out.println("Loaded seed: " + file);
            } catch (Exception ex) {
                System.err.println("Seed file loading failed for " + file + ": " + ex.getMessage());
                // Continue loading other seed files even if one fails
            }
        }
    }
}
