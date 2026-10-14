package com.hcLinear.backendTest.repositories;

import com.hcLinear.backendTest.models.TeamModel;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<TeamModel, Long> {

    boolean existsByName(String name);

    List<TeamModel> findByCaptainId(Long captainId);

    @EntityGraph(attributePaths = {"captain"})
    @Query("SELECT t FROM TeamModel t")
    List<TeamModel> findAllWithCaptain();

    @Query("SELECT t FROM TeamModel t LEFT JOIN FETCH t.captain WHERE t.id = :id")
    Optional<TeamModel> findByIdWithCaptain(@Param("id") Long id);
}
