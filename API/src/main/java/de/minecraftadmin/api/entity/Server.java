package de.minecraftadmin.api.entity;

import javax.persistence.Id;

/**
 * Created with IntelliJ IDEA.
 * User: Dustin
 * Date: 16.12.12
 * Time: 23:08
 * To change this template use File | Settings | File Templates.
 */
public class Server {

    @Id
    private Long id;
    private String serverName;
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
