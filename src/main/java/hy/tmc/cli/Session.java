package hy.tmc.cli;


public class Session {

    private String username;
    private String password;
    
    public Session() {
        this.username = "";
        this.password = "";
    }
    
    public Session(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void clear() {
        username = "";
        password = "";
    }
}
