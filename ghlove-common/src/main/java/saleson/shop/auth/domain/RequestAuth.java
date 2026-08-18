package saleson.shop.auth.domain;

public class RequestAuth {

    private String loginType;
    private String loginId;
    private String password;
    private String financ;
    
    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getFinanc() {
        return financ;
    }

    public void setFinanc(String financ) {
        this.financ = financ;
    }

}
