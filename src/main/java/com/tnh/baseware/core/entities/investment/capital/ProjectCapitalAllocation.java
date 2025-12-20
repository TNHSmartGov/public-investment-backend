package com.tnh.baseware.core.entities.investment.capital;

import com.tnh.baseware.core.entities.audit.Auditable;
import com.tnh.baseware.core.entities.investment.Project;

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
public class ProjectCapitalAllocation extends Auditable<String> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @ManyToOne
    @JoinColumn(name = "project_id")
    Project project;

    @ManyToOne
    @JoinColumn(name = "capital_id")
    CapitalPlan capitalPlan;

    BigDecimal amountInMediumTerm; // Số vốn trung hạn dự án được cấp từ nguồn này
    
    // Liên kết đến kế hoạch hằng năm của riêng nguồn vốn này cho dự án này
    @OneToMany(mappedBy = "projectCapitalAllocation")
    Set<CapitalPlanLine> capitalPlanLines = new HashSet<>();

}

