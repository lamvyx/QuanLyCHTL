package entity;

public class TaiKhoan {
    private Integer maTK;
    private String username;
    private String password;
    private String role;
    private Boolean trangThai;

    public TaiKhoan() {
    }

    public TaiKhoan(Integer maTK, String username, String password, String role, Boolean trangThai) {
        this.maTK = maTK;
        this.username = username;
        this.password = password;
        this.role = role;
        this.trangThai = trangThai;
    }

    public Integer getMaTK() {
        return maTK;
    }

    public void setMaTK(Integer maTK) {
        this.maTK = maTK;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Boolean getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(Boolean trangThai) {
        this.trangThai = trangThai;
    }

    @Override
    public String toString() {
        return "TaiKhoan{" +
                "maTK=" + maTK +
                ", username='" + username + '\'' +
                ", role='" + role + '\'' +
                ", trangThai=" + trangThai +
                '}';
    }
}
