package laby.lab3_javafx;

import java.io.Serializable;

public class Data implements Serializable {
    private String username;
    private String ip;
    private String answer;
    public Data(String username, String ip, String answer) {
        this.username = username;
        this.ip = ip;
        this.answer = answer;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
