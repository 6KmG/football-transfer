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

    @NotNull
    @Column(name = "document_number", nullable = false, unique = true, length = 20)
    private String documentNumber;

    @NotNull
    @Column(name = "sequence_number", nullable = false, unique = true)
    private Long sequenceNumber;

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
