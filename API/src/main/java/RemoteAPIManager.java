import de.minecraftadmin.api.API;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.handler.MessageContext;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author BADMAN152
 * Accessor Class to access/connect to the remote database/service
 */
public class RemoteAPIManager {

    private final String apiKey;
    private final String serviceURL;
    private final String NAMESPACE = "secureBan.minecraftadmin.de";

    public RemoteAPIManager(String serviceURL, String apiKey){
        this.serviceURL = serviceURL;
        this.apiKey = apiKey;
    }

    /**
     * @author BADMAN152
     * maps the remote WSDL file to a Java Object
     * @return API Object
     * @throws Throwable if service is down or configured serviceURL is wrong
     */
    protected API getRemoteAPI()throws Throwable{
        Service remoteCommunicationBeanService = Service.create(
                new URL(serviceURL+"?WSDL"),
                new QName(NAMESPACE, "RemoteApi"));
        API remote = remoteCommunicationBeanService.getPort(API.class);
        Map<String, Object> req_ctx = ((BindingProvider)remote).getRequestContext();
        req_ctx.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, serviceURL+"?WSDL");

        Map<String, List<String>> headers = new HashMap<String, List<String>>();
        headers.put("apikey", Collections.singletonList(apiKey));
        req_ctx.put(MessageContext.HTTP_REQUEST_HEADERS, headers);
        return remote;
    }

}
