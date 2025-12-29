package com.tnh.baseware.core.mappers.user;

import com.tnh.baseware.core.components.GenericEntityFetcher;
import com.tnh.baseware.core.dtos.user.UserDTO;
import com.tnh.baseware.core.entities.user.User;
import com.tnh.baseware.core.forms.user.UserEditorForm;
import com.tnh.baseware.core.mappers.IGenericMapper;
import com.tnh.baseware.core.repositories.adu.IOrganizationRepository;
import com.tnh.baseware.core.repositories.user.IRoleRepository;

import org.mapstruct.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IUserMapper extends IGenericMapper<User, UserEditorForm, UserDTO> {

    @Mapping(target = "roles", expression = "java(fetcher.formToEntities(roleRepository, form.getRole()))")
    @Mapping(target = "organizations", expression = "java(fetcher.fetchEntitiesByIds(organizationRepository, form.getOrganizationIds()))")
    @Mapping(target = "password", source = "password", qualifiedByName = "passwordToPassword")
    User formToEntity(UserEditorForm form,
            @Context GenericEntityFetcher fetcher,
            @Context IRoleRepository roleRepository,
            @Context IOrganizationRepository organizationRepository,
            @Context PasswordEncoder passwordEncoder);

    @Mapping(target = "roles", expression = "java(fetcher.formToEntities(roleRepository, form.getRole()))")
    @Mapping(target = "organizations", expression = "java(fetcher.fetchEntitiesByIds(organizationRepository, form.getOrganizationIds()))")
    @Mapping(target = "password", ignore = true)
    void updateUserFromForm(UserEditorForm form,
            @MappingTarget User user,
            @Context GenericEntityFetcher fetcher,
            @Context IRoleRepository roleRepository,
            @Context IOrganizationRepository organizationRepository);

    @Named("passwordToPassword")
    default String passwordToPassword(String password, @Context PasswordEncoder passwordEncoder) {
        return passwordEncoder.encode(password);
    }

}
