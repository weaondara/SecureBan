package persistence.Utils;

import de.minecraftadmin.api.API;
import de.minecraftadmin.api.RemoteAPIManager;
import de.minecraftadmin.api.entity.BanType;
import de.minecraftadmin.api.entity.Player;
import de.minecraftadmin.api.entity.PlayerBan;
import de.minecraftadmin.api.jaxws.Login;
import junit.framework.Assert;

/**
 * Created with IntelliJ IDEA.
 * User: Dustin
 * Date: 24.12.12
 * Time: 10:11
 * To change this template use File | Settings | File Templates.
 */
public class FakeRemoteAPIManager extends RemoteAPIManager {
    public FakeRemoteAPIManager(String serviceURL, String apiKey) {
        super(serviceURL, apiKey);
    }

    /**
     * @return API Object
     * @throws Throwable if service is down or configured serviceURL is wrong
     * @author BADMAN152
     * maps the remote WSDL file to a Java Object
     */
    @Override
    public API getRemoteAPI() throws Throwable {
        return new API() {
            @Override
            public Login allowedToJoin(String playerName) {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public Player getPlayerBans(String playerName) {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void submitPlayerBans(String playerName, PlayerBan ban) {
                Assert.assertEquals("Submit only Global Bans", BanType.GLOBAL, ban.getBanType());
            }

            @Override
            public void unBanPlayer(String playerName, Long ban) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public String getAPIVersion() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }
        };
    }
}
