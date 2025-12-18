package com.tnh.baseware.core.entities.user;

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
@Table(schema = "master")
public class Tenant extends Auditable<String> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Column(nullable = false, unique = true)
    String name;

    @Column(nullable = false, unique = true)
    String schemaName;

    @Column(nullable = false)
    @Builder.Default
    Boolean active = Boolean.TRUE;

    // @Column(name = "audit_schema_name") // Thêm field này
    // String auditSchemaName;

    // @Column(name = "audit_enabled") // Thêm field này
    // @Builder.Default
    // Boolean auditEnabled = Boolean.TRUE;

}
