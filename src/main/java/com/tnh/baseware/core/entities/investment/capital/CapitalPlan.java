package com.tnh.baseware.core.entities.investment.capital;

import com.tnh.baseware.core.entities.audit.Auditable;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
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
public class CapitalPlan extends Auditable<String> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Column(nullable = false)
    String code;

    @Column(nullable = false)
    String name;

    @Column(nullable = false)
    Integer startYear;

    @Column(nullable = false)
    Integer endYear;

    @Column(nullable = false)
    BigDecimal totalAmountPlan;

    @Column(nullable = false)
    Boolean isApproved;

    String description;

    @ManyToOne
    @JoinColumn(name = "capital_id") // Liên kết đến danh mục nguồn vốn
    Capital capital;

    @OneToMany(mappedBy = "capitalPlan")
    Set<ProjectCapitalAllocation> projectAllocations = new HashSet<>();

}
