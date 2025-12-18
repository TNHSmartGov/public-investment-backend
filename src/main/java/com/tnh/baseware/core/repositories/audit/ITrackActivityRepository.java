package com.tnh.baseware.core.repositories.audit;

import com.tnh.baseware.core.dtos.audit.TrackActivityDetailDTO;
import com.tnh.baseware.core.entities.audit.TrackActivity;
import com.tnh.baseware.core.repositories.IGenericRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ITrackActivityRepository extends IGenericRepository<TrackActivity, UUID> {

    @Transactional(readOnly = true)
    @Query("SELECT new com.tnh.baseware.core.dtos.audit.TrackActivityDetailDTO(t.requestPayload, t.responsePayload) " +
            "FROM TrackActivity t " +
            "WHERE t.id = :id")
    Optional<TrackActivityDetailDTO> getTrackActivityDetail(UUID id);
}

