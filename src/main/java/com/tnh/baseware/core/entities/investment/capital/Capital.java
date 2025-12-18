package com.tnh.baseware.core.entities.investment.capital;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tnh.baseware.core.annotations.ScanableEntity;
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
@ScanableEntity(name="Capital", alias="capital", description="Represents a capital source")
@FieldDefaults(level = AccessLevel.PRIVATE)
//Nguồn vốn
public class Capital extends Auditable<String> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Column(nullable = false)
    String code; //mã nguồn vốn
    
    @Column(nullable = false)
    String name; //tên nguồn vốn

    @Column(nullable = false)
    String level; //Cấp quản lý: CAP_TINH, CAP_XA
    
    String shortName;//viết tắt

    String description; //mô tả
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    Capital parent;

    @JsonIgnore
    @ManyToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    @Builder.Default
    Set<Capital> children = new HashSet<>();

}
