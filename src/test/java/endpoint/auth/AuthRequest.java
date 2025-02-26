package endpoint.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AuthRequest(@JsonProperty("username") String login, @JsonProperty("password") String password) {
}
