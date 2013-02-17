package de.minecraftadmin.api.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Hold the informations for the Maintenance announcement
 */
@Entity
public class Maintenance implements Serializable {

    @Id

    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private Long startTime;
    @Column(nullable = false)
    private Long endTime;
    @Column(nullable = false)
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long end) {
        this.endTime = end;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long start) {
        this.startTime = start;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
