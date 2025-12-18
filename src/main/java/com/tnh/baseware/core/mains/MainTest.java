package com.tnh.baseware.core.mains;

import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public class MainTest {

    public static void main(String[] args) {
        UUID uuid = UUID.randomUUID();
        log.info("Generated UUID: {}", uuid);
    }
}