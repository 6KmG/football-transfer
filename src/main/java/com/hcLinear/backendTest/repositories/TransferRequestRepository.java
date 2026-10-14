package com.hcLinear.backendTest.repositories;

import com.hcLinear.backendTest.enums.TransferStatus;
import com.hcLinear.backendTest.models.TransferRequestModel;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferRequestRepository extends JpaRepository<TransferRequestModel, Long> {

    List<TransferRequestModel> findByPlayerId(Long playerId);

    List<TransferRequestModel> findByPlayerIdAndStatus(Long playerId, TransferStatus status);

    List<TransferRequestModel> findByStatus(TransferStatus status);

    void deleteByPlayerId(Long playerId);

    void deleteBySourceTeamIdOrTargetTeamId(Long sourceTeamId, Long targetTeamId);

    @EntityGraph(attributePaths = {"player", "sourceTeam", "targetTeam"})
    @Query("SELECT t FROM TransferRequestModel t WHERE t.id = :id")
    java.util.Optional<TransferRequestModel> findWithRelationsById(@Param("id") Long id);

    @EntityGraph(attributePaths = {"player", "sourceTeam", "targetTeam"})
    @Query("SELECT t FROM TransferRequestModel t "
            + "WHERE (:playerId IS NULL OR t.player.id = :playerId) "
            + "AND (:sourceTeamId IS NULL OR t.sourceTeam.id = :sourceTeamId) "
            + "AND (:targetTeamId IS NULL OR t.targetTeam.id = :targetTeamId) "
            + "AND (:status IS NULL OR t.status = :status)")
    List<TransferRequestModel> findFiltered(@Param("playerId") Long playerId,
                                           @Param("sourceTeamId") Long sourceTeamId,
                                           @Param("targetTeamId") Long targetTeamId,
                                           @Param("status") TransferStatus status);
}
