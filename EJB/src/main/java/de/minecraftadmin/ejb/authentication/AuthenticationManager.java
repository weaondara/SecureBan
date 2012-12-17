package de.minecraftadmin.ejb.authentication;

import javax.annotation.Resource;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import java.util.List;
import java.util.Map;

/**
 * authenticate each call via api-Key
 */
@Interceptor
public class AuthenticationManager {
    @Resource
    private WebServiceContext webservice;

    @AroundInvoke
    public Object authenticate(InvocationContext ic) throws Exception {
        MessageContext messageContext = webservice.getMessageContext();
        Map headerData = (Map) messageContext.get(MessageContext.HTTP_REQUEST_HEADERS);
        HttpServletRequest req = (HttpServletRequest) messageContext.get(MessageContext.SERVLET_REQUEST);
        List keys = (List) headerData.get("apikey");
        if (keys != null) {
            String password = keys.get(0).toString();
        }

        return ic.proceed();
    }
}
