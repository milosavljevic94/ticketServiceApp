package com.ftn.model;

import javax.persistence.*;

@Entity
@Table(name = "reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    private User user;

    private Integer expDays;

    private Boolean active;

    //Information which ticket is reserved.

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ticket_id", referencedColumnName = "id")
    private Ticket ticket;

    public Reservation() {
    }

    public Reservation(User user, Integer expDays, Boolean active) {
        this.user = user;
        this.expDays = expDays;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getExpDays() {
        return expDays;
    }

    public void setExpDays(Integer expDays) {
        this.expDays = expDays;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", user=" + user +
                ", expDays=" + expDays +
                ", active=" + active +
                '}';
    }
}
