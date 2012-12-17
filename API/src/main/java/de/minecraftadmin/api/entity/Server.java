package de.minecraftadmin.api.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author BADMAN152
 * Represent each server who is allowed to communicate with the remote service
 */
@Entity
public class Server implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false, unique = true)
    private String serverName;
    @Column(nullable = false)
    private String apiKey;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
