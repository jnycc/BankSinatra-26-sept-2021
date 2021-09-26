package com.miw.service;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.*;


@SpringBootTest
class CryptoPriceServiceTest {

    private final Logger logger = LoggerFactory.getLogger(CryptoPriceServiceTest.class);
    private CryptoPriceService cryptoPriceService;

    @Autowired
    public CryptoPriceServiceTest(CryptoPriceService cryptoPriceService) {
        super();
        this.cryptoPriceService = cryptoPriceService;
        logger.info("New CryptoPriceService Integration Test");
    }

    @Test
    void integrationTest() {
        assertThat(cryptoPriceService.getRootRepository()).isNotNull();
    }

    @Test
    void timeStampFormatTest() {
        String apiTimestampFormat = "\"2021-09-06T15:41:36.991Z\""; // note: this is without the 2-hour timezone offset
        LocalDateTime expectedResult = LocalDateTime.of(2021, 9, 6, 17, 41, 36);
        LocalDateTime correctedFormat = cryptoPriceService.correctTimestampFormatting(apiTimestampFormat);
        assertThat(correctedFormat).isEqualTo(expectedResult);
    }
}