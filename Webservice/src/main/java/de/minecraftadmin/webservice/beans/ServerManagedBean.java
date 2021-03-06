package de.minecraftadmin.webservice.beans;

import de.minecraftadmin.api.entity.Note;
import de.minecraftadmin.api.entity.PlayerBan;
import de.minecraftadmin.api.entity.Server;
import de.minecraftadmin.ejb.beans.DatabaseService;
import org.apache.commons.codec.binary.Hex;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Dustin
 * Date: 23.12.12
 * Time: 11:19
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean(name = "ServerManager")
@SessionScoped
public class ServerManagedBean {

    @EJB
    private DatabaseService database;
    private String serverName;

    public void addNewServer() {
        String apiKey = serverName + System.currentTimeMillis();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.reset();
            md.update(apiKey.getBytes("UTF8"));
            apiKey = new String(Hex.encodeHex(md.digest()));
        } catch (Throwable e) {
            apiKey = apiKey.hashCode() + "";
        }
        Server server = new Server();
        server.setServerName(serverName);
        server.setApiKey(apiKey);
        database.persist(server);
        serverName = null;
    }

    public void deleteServer(Server server) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("name", "Deleted Server");
        Server holder = database.getSingleResult(Server.class, "SELECT s FROM Server s WHERE s.serverName=:name", params);
        if (holder == null) {
            this.serverName = "Deleted Server";
            this.addNewServer();
            deleteServer(server);
            return;
        }
        params.clear();
        params.put("serverID", holder.getId());
        List<PlayerBan> players = database.getResultList(PlayerBan.class, "SELECT p FROM PlayerBan p WHERE p.server.id=:serverID", params);
        for (PlayerBan p : players) {
            p.setServer(holder);
            database.update(p);
        }
        List<Note> notes = database.getResultList(Note.class, "SELECT n FROM Note n WHERE n.server.id=:serverID", params);
        for (Note n : notes) {
            n.setServer(holder);
            database.update(n);
        }
        database.delete(server);

    }

    public List<Server> serverList() {
        List<Server> servers = database.getResultList(Server.class, "SELECT s FROM Server s", new HashMap<String, Object>());
        if (servers == null) return new ArrayList<Server>();
        return servers;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }
}
