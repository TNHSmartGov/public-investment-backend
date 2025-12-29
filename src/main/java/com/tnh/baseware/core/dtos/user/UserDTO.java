package com.tnh.baseware.core.dtos.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tnh.baseware.core.entities.audit.Identifiable;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.hateoas.RepresentationModel;

import com.tnh.baseware.core.dtos.adu.OrganizationDTO;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDTO extends RepresentationModel<UserDTO> implements Identifiable<UUID> {

    UUID id;
    String username;

    @JsonIgnore
    String password;
    String firstName;
    String lastName;
    String fullName;
    String phone;
    String email;
    String avatarUrl;
    String idn;
    String address;
    Integer ial;
    Boolean enabled;
    Boolean locked;
    Instant lockTime;
    Instant accountExpiryDate;
    Integer failedLoginAttempts;
    Boolean superAdmin;
    String userType;
    List<RoleDTO> roles;
    Set<OrganizationDTO> organizations;
}