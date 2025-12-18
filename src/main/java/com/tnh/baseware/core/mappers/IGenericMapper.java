package com.tnh.baseware.core.mappers;

import org.mapstruct.MappingTarget;

import java.util.List;

public interface IGenericMapper<E, F, D> {

    E formToEntity(F form);

    void formToEntity(F form, @MappingTarget E entity);

    F entityToForm(E entity);

    void entityToForm(E entity, @MappingTarget F form);

    E dtoToEntity(D dto);

    void dtoToEntity(D dto, @MappingTarget E entity);

    D entityToDTO(E entity);

    void entityToDTO(E entity, @MappingTarget D dto);

    List<E> formsToEntities(List<F> forms);

    void formsToEntities(List<F> forms, @MappingTarget List<E> entities);

    List<F> entitiesToForms(List<E> entities);

    void entitiesToForms(List<E> entities, @MappingTarget List<F> forms);

    List<E> dtosToEntities(List<D> dtos);

    void dtosToEntities(List<D> dtos, @MappingTarget List<E> entities);

    List<D> entitiesToDTOs(List<E> entities);

    void entitiesToDTOs(List<E> entities, @MappingTarget List<D> dtos);
}