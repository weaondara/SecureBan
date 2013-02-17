package de.minecraftadmin.api;

import de.minecraftadmin.api.entity.Maintenance;
import de.minecraftadmin.api.entity.Note;
import de.minecraftadmin.api.entity.Player;
import de.minecraftadmin.api.entity.PlayerBan;
import de.minecraftadmin.api.generated.Version;
import de.minecraftadmin.api.jaxws.Login;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.handler.MessageContext;
import java.net.URL;
import java.util.*;

/**
 * @author BADMAN152
 *         Accessor Class to access/connect to the remote database/service
 */
public class RemoteAPIManager implements API {

    public static int TIMEOUT = 8000;
    private final String apiKey;
    private final String serviceURL;
    private final String NAMESPACE = "http://minecraftadmin.de/secureban";
    private Maintenance maintenance;
    private boolean update = false;
    private String versionName = Version.name;
    private String versionMessage = "";

    public RemoteAPIManager(String serviceURL, String apiKey) {
        this.serviceURL = serviceURL;
        this.apiKey = apiKey;
    }

    /**
     * @return API Object
     * @throws Throwable if service is down or configured serviceURL is wrong
     * @author BADMAN152
     * maps the remote WSDL file to a Java Object
     */
    protected API getRemoteAPI() throws Exception {
        Service remoteCommunicationBeanService = Service.create(
                new URL(serviceURL + "?WSDL"),
                new QName(NAMESPACE, "RemoteApi"));
        API remote = remoteCommunicationBeanService.getPort(API.class);
        Map<String, Object> req_ctx = ((BindingProvider) remote).getRequestContext();
        req_ctx.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, serviceURL + "?WSDL");

        req_ctx.put("com.sun.xml.internal.ws.connect.timeout", TIMEOUT);
        req_ctx.put("com.sun.xml.internal.ws.request.timeout", TIMEOUT);

        Map<String, List<String>> headers = new HashMap<String, List<String>>();
        headers.put("apikey", Collections.singletonList(apiKey));
        headers.put("version", Collections.singletonList(Version.name));
        req_ctx.put(MessageContext.HTTP_REQUEST_HEADERS, headers);
        return remote;
    }

    private void filterMetaData(Object remote) {
        Map<String, Object> resp_ctx = ((BindingProvider) remote).getResponseContext();
        Map<String, List<Object>> response = (Map<String, List<Object>>) resp_ctx.get(MessageContext.HTTP_RESPONSE_HEADERS);
        if (!response.containsKey("maintenance") || !response.containsKey("version")) return;

        // this is a bit hacky style i dont know why jaxws cant parse header list by himself
        List<String> maintenanceData = Arrays.asList(response.get("maintenance").toString().replace("[", "").replace("]", "").split(","));
        List<String> versionData = Arrays.asList(response.get("version").toString().replace("[", "").replace("]", "").split(","));

        if (!maintenanceData.isEmpty()) {
            Maintenance m = new Maintenance();
            m.setStartTime(Long.valueOf(maintenanceData.get(0)));
            m.setEndTime(Long.valueOf(maintenanceData.get(1)));
            m.setMessage(maintenanceData.get(2));
            this.maintenance = m;
        }

        if (!versionData.isEmpty()) {
            int identifier = Integer.parseInt(versionData.get(2));
            this.versionName = versionData.get(0);
            this.versionMessage = versionData.get(1);
            if (identifier == Version.OLD || identifier == Version.UNKNOWN) this.update = true;
            else this.update = false;
        }

    }

    public Maintenance getMaintenance() {
        return maintenance;
    }

    @Override
    public Login allowedToJoin(String playerName) throws Exception {
        API remote = getRemoteAPI();
        try {
            return remote.allowedToJoin(playerName);
        } finally {
            filterMetaData(remote);
        }
    }

    @Override
    public Player getPlayerBans(String playerName) throws Exception {
        API remote = getRemoteAPI();
        try {
            return remote.getPlayerBans(playerName);
        } finally {
            filterMetaData(remote);
        }
    }

    @Override
    public void submitPlayerBans(String playerName, PlayerBan ban) throws Exception {
        API remote = getRemoteAPI();
        try {
            remote.submitPlayerBans(playerName, ban);
        } finally {
            filterMetaData(remote);
        }
    }

    @Override
    public void unBanPlayer(String playerName, Long expire) throws Exception {
        API remote = getRemoteAPI();
        try {
            remote.unBanPlayer(playerName, expire);
        } finally {
            filterMetaData(remote);
        }
    }

    @Override
    public void sumitPlayerNote(String playerName, Note playerNote) {
        API remote = null;
        try {
            remote = getRemoteAPI();
            remote.sumitPlayerNote(playerName, playerNote);
        } catch (Throwable throwable) {
            throwable.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            filterMetaData(remote);
        }
    }

    @Override
    public void deletePlayerNote(String playerName, Long noteId) throws Exception {
        API remote = getRemoteAPI();
        try {
            remote.deletePlayerNote(playerName, noteId);
        } finally {
            filterMetaData(remote);
        }
    }

    @Override
    public List<Note> getPlayerNote(String playerName) throws Exception {
        API remote = getRemoteAPI();
        try {
            return remote.getPlayerNote(playerName);
        } finally {
            filterMetaData(remote);
        }
    }

    @Override
    @Deprecated
    public String getAPIVersion() throws Exception {
        API remote = getRemoteAPI();
        try {
            return remote.getAPIVersion();
        } finally {
            filterMetaData(remote);
        }
    }
}
