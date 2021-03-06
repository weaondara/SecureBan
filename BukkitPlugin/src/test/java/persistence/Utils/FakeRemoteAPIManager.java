package persistence.Utils;

import de.minecraftadmin.api.API;
import de.minecraftadmin.api.RemoteAPIManager;
import de.minecraftadmin.api.entity.BanType;
import de.minecraftadmin.api.entity.Note;
import de.minecraftadmin.api.entity.Player;
import de.minecraftadmin.api.entity.PlayerBan;
import de.minecraftadmin.api.jaxws.Login;
import junit.framework.Assert;

import java.util.List;

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
    protected API getRemoteAPI() throws Exception {
        return new API() {
           /* @Override
            public Login allowedToJoin(String playerName) {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }*/

            @Override
            public Login allowedToJoin(String playerName, String ipHash) throws Exception {
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
            public void sumitPlayerNote(String playerName, Note playerNote) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void deletePlayerNote(String playerName, Long noteId) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public List<Note> getPlayerNote(String playerName) {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

/*            @Override
            public String getAPIVersion() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }*/
        };
    }
}
