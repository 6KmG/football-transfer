package com.hcLinear.backendTest.services;

import org.springframework.stereotype.Component;

@Component
public class TransferDocumentNumberGenerator {

    public static final String PREFIX = "ATIG";
    public static final String SUFFIX = "HU";
    public static final int NUMBER_LENGTH = 6;

    public String generate(long sequenceNumber) {
        if (sequenceNumber <= 0) {
            throw new IllegalArgumentException("A futó sorszámnak pozitívnak kell lennie: " + sequenceNumber);
        }
        String padded = String.format("%0" + NUMBER_LENGTH + "d", sequenceNumber);
        return PREFIX + padded + SUFFIX;
    }
}
