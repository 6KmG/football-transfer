package com.hcLinear.backendTest.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Sikeres átigazolást érvényesítő dokumentum.
 *
 * <p>Minden sikeres átigazolás (átigazolási kérelem elfogadása) esetén pontosan egy
 * dokumentum jön létre. Emberileg olvasható, egyedi azonosítója
 * {@code prefix + futó sorszám + suffix} felépítésű, pl. {@code ATIG000014HU},
 * ahol a futó sorszám fix hosszúságú, nullákkal feltöltött, növekvő sorszámozás.</p>
 */
@Entity
@Table(name = "transfer_documents")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransferDocumentModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Emberileg olvasható egyedi azonosító, pl. ATIG000014HU.
     */
    @NotNull
    @Column(name = "document_number", nullable = false, unique = true, length = 20)
    private String documentNumber;

    /**
     * A dokumentumszámban szereplő futó sorszám (pl. 14 -> "000014").
     * Adatbázis-szekvenciából származik, így konkurrens végrehajtás esetén is egyedi és növekvő.
     */
    @NotNull
    @Column(name = "sequence_number", nullable = false, unique = true)
    private Long sequenceNumber;

    /**
     * Az átigazolás végrehajtásának dátuma (az elfogadás napja).
     */
    @NotNull
    @Column(name = "transfer_date", nullable = false)
    private LocalDate transferDate;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "player_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private PlayerModel player;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_team_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private TeamModel sourceTeam;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "target_team_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private TeamModel targetTeam;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    @Column(name = "fee", nullable = false, precision = 15, scale = 2)
    private BigDecimal fee;

    /**
     * Az alapul szolgáló átigazolási kérelem. Egy kérelemhez legfeljebb egy dokumentum
     * tartozhat (sikeres elfogadás esetén pontosan egy).
     */
    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "transfer_request_id", nullable = false, unique = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private TransferRequestModel transferRequest;

    @NotNull
    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
