package com.github.vinicius2335.planner;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThatCode;

class RandomTest {

    @Test
    void generate_LocalDateTime(){
        assertThatCode(() -> {
            DateTimeFormatter isoDateTime = DateTimeFormatter.ISO_DATE_TIME;

            LocalDateTime now = LocalDateTime.now();

            System.out.println(isoDateTime.format(now));
        })
                .doesNotThrowAnyException();
    }

    @Test
    void LocalDateTime_toString(){
        assertThatCode(() -> {
            LocalDateTime now = LocalDateTime.now();
            String nowString = now.toString();

            System.out.println(nowString);
        })
                .doesNotThrowAnyException();
    }
}
