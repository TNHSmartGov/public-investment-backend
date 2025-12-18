package com.tnh.baseware.core.entities.adu;

import com.tnh.baseware.core.annotations.ScanableEntity;
import com.tnh.baseware.core.entities.audit.Auditable;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@ScanableEntity(name = "Province", alias = "province", description = "Represents a province")
public class Province extends Auditable<String> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Column(unique = true, nullable = false)
    String code;

    @Column(nullable = false)
    String name;

    @Column(nullable = false)
    String countryCode;

    String longitude;
    String latitude;
}
