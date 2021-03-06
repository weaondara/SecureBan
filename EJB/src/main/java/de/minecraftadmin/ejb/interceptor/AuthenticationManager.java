package de.minecraftadmin.ejb.interceptor;

import de.minecraftadmin.api.entity.Server;
import de.minecraftadmin.ejb.beans.DatabaseService;
import org.apache.log4j.Logger;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * authenticate each call via api-Key
 */
public class AuthenticationManager {

    private final Logger LOG = Logger.getLogger("SecurityManager");
    @EJB
    private DatabaseService databaseService;
    @Resource
    private WebServiceContext webservice;

    @AroundInvoke
    public Object authenticate(InvocationContext ic) throws Exception {
        MessageContext messageContext = webservice.getMessageContext();
        Map headerData = (Map) messageContext.get(MessageContext.HTTP_REQUEST_HEADERS);
        List keys = (List) headerData.get("apikey");
        Server s = null;
        String apikey = "NONE";
        if (keys != null) {
            apikey = keys.get(0).toString();
            HashMap<String, Object> param = new HashMap<String, Object>();
            param.put("key", apikey);
            s = databaseService.getSingleResult(Server.class, "SELECT s FROM Server s WHERE s.apiKey=:key", param);
        }
        if (s == null) throw new SecurityException("Could not Authenticate request with apiKey: " + apikey);
        LOG.info("Authenticated Server " + s.getServerName());
        LOG.info("triggered method " + ic.getMethod().getName());
        headerData.put("server", Collections.singletonList(s));

        return ic.proceed();
    }
}
