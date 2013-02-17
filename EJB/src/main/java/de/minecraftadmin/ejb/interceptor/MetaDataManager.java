package de.minecraftadmin.ejb.interceptor;

import de.minecraftadmin.api.entity.Maintenance;
import de.minecraftadmin.api.generated.Version;
import de.minecraftadmin.ejb.beans.MaintenanceService;
import org.apache.log4j.Logger;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * mapping the version of the request and the version of the webservice
 */
public class MetaDataManager {

    private Logger LOG = Logger.getLogger("MetaDataManager");

    @EJB
    private MaintenanceService maintenanceService;

    @Resource
    private WebServiceContext webservice;

    @AroundInvoke
    public Object addMetaData(InvocationContext ic) throws Exception {
        MessageContext messageContext = webservice.getMessageContext();
        Map headerData = (Map) messageContext.get(MessageContext.HTTP_REQUEST_HEADERS);
        List keys = (List) headerData.get("version");
        String version = (String) keys.get(0);
        if (version == null) version = "UNKNOWN"; // should never happen
        Map<Object, Object> responseData = (Map<Object, Object>) messageContext.get(MessageContext.HTTP_RESPONSE_HEADERS);
        if (responseData == null) responseData = new HashMap<Object, Object>();
        List<Object> versionMapper = new ArrayList<Object>();
        versionMapper.add(Version.name);
        try {
            int versionNumber = Integer.parseInt(version.split("-")[0].replace("\\.", ""));
            int myVersionNumber = Integer.parseInt(Version.name.split("-")[0].replace("\\.", ""));
            if (versionNumber < myVersionNumber) {
                versionMapper.add("You running an older version please update");
                versionMapper.add(Version.OLD);
            } else if (versionNumber > myVersionNumber) {
                versionMapper.add("You running a dev Version be aware");
                versionMapper.add(Version.DEV);
            } else {
                versionMapper.add("");
                versionMapper.add(Version.SAME);
            }
        } catch (NumberFormatException e) {
            versionMapper.add("You running on a different Version");
            versionMapper.add(Version.UNKNOWN);
        }
        responseData.put("version", versionMapper);

        Maintenance m = maintenanceService.getMaintenance();
        if (m != null) {
            List<Object> maintenanceData = new ArrayList<Object>();
            maintenanceData.add(m.getStartTime());
            maintenanceData.add(m.getEndTime());
            maintenanceData.add(m.getMessage());
            responseData.put("maintenance", maintenanceData);
        }
        messageContext.put(MessageContext.HTTP_RESPONSE_HEADERS, responseData);
        return ic.proceed();
    }
}