package de.minecraftadmin.api.entity;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: Dustin
 * Date: 25.03.13
 * Time: 23:26
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class IPAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false, unique = true)
    private String ipHash;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIpHash() {
        return ipHash;
    }

    public void setIpHash(String ipHash) {
        this.ipHash = ipHash;
    }
}
