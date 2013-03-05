package de.minecraftadmin.webservice.beans.model;

import de.minecraftadmin.api.entity.Player;
import de.minecraftadmin.ejb.beans.DatabaseService;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Dustin
 * Date: 03.03.13
 * Time: 21:54
 * To change this template use File | Settings | File Templates.
 */
public class LazyLoadPlayerModel extends LazyDataModel<Player> {

    private DatabaseService database;
    private List<Player> players;
    private int startAt;
    private int endAt;
    private int pageSize;

    public LazyLoadPlayerModel(DatabaseService service) {
        this.database = service;
    }

    @Override
    public List<Player> load(int startAt, int endAt, String sortField, SortOrder sortOrder, Map<String, String> filters) {

        HashMap<String, Object> params = new HashMap<String, Object>();
        if (!filters.isEmpty()) {
            params = (HashMap) filters;
            params.put("userName", "%" + params.get("userName") + "%");
            players = database.getResultListWithLimit(Player.class, "SELECT p FROM Player p WHERE p.userName like :userName", params, startAt, endAt);
            this.setRowCount(((Long) database.querySingeResult("SELECT count(*) FROM Player p WHERE p.userName like :userName", params)).intValue());
        } else {
            players = database.getResultListWithLimit(Player.class, "SELECT p FROM Player p", params, startAt, endAt);
            this.setRowCount(((Long) database.querySingeResult("SELECT count(*) FROM Player p", new HashMap<String, Object>())).intValue());
        }
        this.setWrappedData(players);
        this.setPageSize(endAt);
        this.setRowIndex(startAt);
        return players;
    }

    @Override
    public Object getRowKey(Player object) {
        return object.getId().toString();
    }

    @Override
    public Player getRowData(String rowKey) {
        for (Player p : players) {
            if (p.getId().toString().equals(rowKey)) return p;
        }
        return null;
    }

    @Override
    public void setRowIndex(int rowIndex) {
        if (this.getPageSize() == 0) this.setPageSize(1);
        super.setRowIndex(rowIndex);
    }
}
