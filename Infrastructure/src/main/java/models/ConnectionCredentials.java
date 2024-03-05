package models;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ConnectionCredentials {
    private String POSTGRESQL_HOST;
    private String POSTGRESQL_USER;
    private String POSTGRESQL_PORT;
    private String POSTGRESQL_PASSWORD;
    private String POSTGRESQL_DATABASE_NAME;
}
