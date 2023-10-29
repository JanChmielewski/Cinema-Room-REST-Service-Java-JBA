package cinema;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CustomError {

    @JsonProperty("error")
    private String message;

    public CustomError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
