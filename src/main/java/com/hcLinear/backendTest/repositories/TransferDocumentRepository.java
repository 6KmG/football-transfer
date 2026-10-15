package com.hcLinear.backendTest.repositories;

import com.hcLinear.backendTest.models.TransferDocumentModel;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferDocumentRepository extends JpaRepository<TransferDocumentModel, Long> {

    @Query(value = "SELECT nextval('transfer_document_seq')", nativeQuery = true)
    Long nextSequenceValue();

    Optional<TransferDocumentModel> findByDocumentNumber(String documentNumber);

    Optional<TransferDocumentModel> findByTransferRequestId(Long transferRequestId);

    boolean existsByTransferRequestId(Long transferRequestId);

    void deleteByPlayerId(Long playerId);

    void deleteBySourceTeamIdOrTargetTeamId(Long sourceTeamId, Long targetTeamId);

    @EntityGraph(attributePaths = {"player", "sourceTeam", "targetTeam", "transferRequest"})
    @Query("SELECT d FROM TransferDocumentModel d WHERE d.id = :id")
    Optional<TransferDocumentModel> findWithRelationsById(@Param("id") Long id);

    @EntityGraph(attributePaths = {"player", "sourceTeam", "targetTeam", "transferRequest"})
    @Query("SELECT d FROM TransferDocumentModel d WHERE d.documentNumber = :documentNumber")
    Optional<TransferDocumentModel> findWithRelationsByDocumentNumber(
            @Param("documentNumber") String documentNumber);

    @EntityGraph(attributePaths = {"player", "sourceTeam", "targetTeam", "transferRequest"})
    @Query("SELECT d FROM TransferDocumentModel d "
            + "WHERE (:playerId IS NULL OR d.player.id = :playerId) "
            + "AND (:sourceTeamId IS NULL OR d.sourceTeam.id = :sourceTeamId) "
            + "AND (:targetTeamId IS NULL OR d.targetTeam.id = :targetTeamId) "
            + "AND (:documentNumber IS NULL OR d.documentNumber = :documentNumber)")
    List<TransferDocumentModel> findFiltered(@Param("playerId") Long playerId,
                                            @Param("sourceTeamId") Long sourceTeamId,
                                            @Param("targetTeamId") Long targetTeamId,
                                            @Param("documentNumber") String documentNumber);
}
