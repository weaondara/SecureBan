package de.minecraftadmin.api.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * @author BADMAN152
 * represent a player
 */
@Entity
public class Player implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false,unique = true)
    private String userName;
    @OneToMany(cascade = {CascadeType.ALL},fetch = FetchType.EAGER)
    private Set<PlayerBan> bans;

    public Set<PlayerBan> getBans() {
        return bans;
    }

    public void setBans(Set<PlayerBan> bans) {
        this.bans = bans;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
