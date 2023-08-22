package com.example.asyncstockmanager.util;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FunctionalInterface
public interface IgnoreRuntimeException {

    Logger log = LoggerFactory.getLogger(IgnoreRuntimeException.class);

    void run() throws RuntimeException;

    static void ignoredException(IgnoreRuntimeException exception) {
        try {
            exception.run();
        } catch (RuntimeException ex) {
//            log.error(ex.getMessage());
        }
    }
}
