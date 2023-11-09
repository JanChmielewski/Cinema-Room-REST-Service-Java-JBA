package cinema;

public class ReturnRequest {
    private String token;

    ReturnRequest() {
    }

    ReturnRequest(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
