package com.ftn.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "manifestation_days")
public class ManifestationDays {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private LocalDateTime startTime;

    @ManyToOne
    @JoinColumn(name = "manifestation_id")
    private Manifestation manifestation;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "manifestationDays")
    private Set<Ticket> tickets = new HashSet<>();

    //TO DO : Relation to man-sector, because there is price for ticket for that sector.
}
