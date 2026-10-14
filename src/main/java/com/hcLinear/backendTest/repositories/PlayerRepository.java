package com.hcLinear.backendTest.repositories;

import com.hcLinear.backendTest.models.PlayerModel;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<PlayerModel, Long> {

    long countByTeamId(Long teamId);

    List<PlayerModel> findByTeamId(Long teamId);

    @EntityGraph(attributePaths = {"team"})
    @Query("SELECT p FROM PlayerModel p")
    List<PlayerModel> findAllWithTeam();

    @EntityGraph(attributePaths = {"team"})
    @Query("SELECT p FROM PlayerModel p WHERE p.id = :id")
    Optional<PlayerModel> findWithTeamById(@Param("id") Long id);
}