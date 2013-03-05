package de.minecraftadmin.webservice.beans;

import de.minecraftadmin.api.entity.Player;
import de.minecraftadmin.api.entity.PlayerBan;
import de.minecraftadmin.api.generated.Version;
import de.minecraftadmin.ejb.beans.DatabaseService;
import de.minecraftadmin.webservice.beans.model.LazyLoadPlayerModel;
import org.primefaces.model.LazyDataModel;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Dustin
 * Date: 23.12.12
 * Time: 11:20
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean(name = "BanManager")
@ViewScoped
public class BanManagedBean implements Serializable {

    @EJB
    private DatabaseService database;
    private final String version = Version.name;
    private Player selectedPlayer;
    private LazyDataModel<Player> playerList;

    @PostConstruct
    public void init() {
        playerList = new LazyLoadPlayerModel(database);
    }

    public String getVersion() {
        return version;
    }

    public Player getSelectedPlayer() {
        return selectedPlayer;
    }

    public void deleteSingleBanFromSelectedPlayer(PlayerBan ban) {
        selectedPlayer.getBans().remove(ban);
        database.persist(selectedPlayer);
        database.delete(ban);
    }

    public void setSelectedPlayer(Player selectedPlayer) {
        this.selectedPlayer = selectedPlayer;
    }

    public LazyDataModel<Player> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(LazyDataModel<Player> playerList) {
        this.playerList = playerList;
    }
}
