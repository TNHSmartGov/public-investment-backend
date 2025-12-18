package com.tnh.baseware.core.entities.investment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tnh.baseware.core.entities.audit.Auditable;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Agency extends Auditable<String> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Column(nullable = false)
    String code;

    @Column(nullable = false)
    String name;

    String phone;

    String taxNumber;

    @Column(nullable = false)
    String address;

    String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    Agency parent;

    @JsonIgnore
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    @Builder.Default
    Set<Agency> children = new HashSet<>();

}
