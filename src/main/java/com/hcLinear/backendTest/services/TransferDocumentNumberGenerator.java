package com.hcLinear.backendTest.services;

import org.springframework.stereotype.Component;

/**
 * Átigazolási dokumentum emberileg olvasható azonosítójának generálása.
 *
 * <p>Formátum: {@code prefix + futó sorszám + suffix}, ahol a futó sorszám fix
 * hosszúságú, nullákkal feltöltött, növekvő sorszámozás. Példa:
 * {@code ATIG000014HU}.</p>
 */
@Component
public class TransferDocumentNumberGenerator {

    public static final String PREFIX = "ATIG";
    public static final String SUFFIX = "HU";
    public static final int NUMBER_LENGTH = 6;

    /**
     * Dokumentumazonosító képzése a megadott futó sorszámból.
     *
     * @param sequenceNumber a {@code transfer_document_seq} szekvenciából származó érték (1-től indul)
     * @return pl. {@code ATIG000014HU}
     */
    public String generate(long sequenceNumber) {
        if (sequenceNumber <= 0) {
            throw new IllegalArgumentException("A futó sorszámnak pozitívnak kell lennie: " + sequenceNumber);
        }
        String padded = String.format("%0" + NUMBER_LENGTH + "d", sequenceNumber);
        return PREFIX + padded + SUFFIX;
    }
}
