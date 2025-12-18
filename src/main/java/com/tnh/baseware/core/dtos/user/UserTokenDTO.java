package com.tnh.baseware.core.dtos.user;

import com.tnh.baseware.core.entities.audit.Identifiable;

import lombok.*;
import lombok.experimental.FieldDefaults;

import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.tnh.baseware.core.dtos.adu.OrganizationDTO;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserTokenDTO extends RepresentationModel<UserTokenDTO> implements Identifiable<UUID> {

    UUID id;
    String username;
    String firstName;
    String lastName;
    String fullName;
    String phone;
    String email;
    String avatarUrl;
    String idn;
    Integer ial;
    Boolean enabled;
    Boolean locked;
    LocalDateTime lockTime;
    LocalDateTime accountExpiryDate;
    Integer failedLoginAttempts;
    Boolean superAdmin;

    List<MenuDTO> menus;
    List<OrganizationDTO> owners;
}