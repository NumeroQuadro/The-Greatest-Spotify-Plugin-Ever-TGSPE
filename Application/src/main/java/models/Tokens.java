package models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Tokens {
    private String accessToken;
    private String tokenType;
    private Long expiresIn;
    private String scope;
    private Long expiresAt;
    private String refreshToken;
}
