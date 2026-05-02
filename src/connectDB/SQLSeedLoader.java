package connectDB;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLSeedLoader {
    public static void loadSqlFile(String filePath) {
        StringBuilder sql = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                sql.append(line).append("\n");
            }
        } catch (java.io.IOException e) {
            throw new IllegalStateException("Không thể đọc file SQL: " + filePath, e);
        }

        try (Connection con = ConnectDB.getInstance().getConnection();
             Statement st = con.createStatement()) {
            String content = sql.toString();
            String[] batches = content.split("(?i)\\bGO\\b");
            for (String batch : batches) {
                if (batch.trim().length() > 0) {
                    st.execute(batch);
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Lỗi khi chạy SQL seed: " + e.getMessage(), e);
        }
    }
}
