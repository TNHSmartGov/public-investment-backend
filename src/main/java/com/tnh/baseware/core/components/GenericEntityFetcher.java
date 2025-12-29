package com.tnh.baseware.core.components;

import com.tnh.baseware.core.exceptions.BWCNotFoundException;
import com.tnh.baseware.core.repositories.IGenericRepository;
import com.tnh.baseware.core.services.MessageService;
import com.tnh.baseware.core.utils.LogStyleHelper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GenericEntityFetcher {

    MessageService messageService;

    public <T, I> T formToEntity(IGenericRepository<T, I> repository, I id) {
        log.debug(LogStyleHelper.debug("Fetching entity with id: {}"), id);
        return repository.findById(id)
                .orElseThrow(() -> {
                    log.warn(LogStyleHelper.warn("Entity not found with id: {}"), id);
                    return new BWCNotFoundException(messageService.getMessage("entity.not.found", id));
                });
    }

    public <T, I> Set<T> formToEntities(IGenericRepository<T, I> repository, I id) {
        if (id == null) {
            log.debug("Null id provided, returning empty set");
            return new HashSet<>();
        }

        var entity = repository.findById(id).orElseThrow(() -> {
            log.warn("Entity not found with id: {}", id);
            return new BWCNotFoundException(messageService.getMessage("entity.not.found", id));
        });

        Set<T> result = new HashSet<>();
        result.add(entity);
        return result;
    }

    public <T, I> Set<T> fetchEntitiesByIds(IGenericRepository<T, I> repository, java.util.Collection<I> ids) {
        if (ids == null || ids.isEmpty()) {
            return new HashSet<>();
        }
        return new HashSet<>(repository.findAllById(ids));
    }
}
