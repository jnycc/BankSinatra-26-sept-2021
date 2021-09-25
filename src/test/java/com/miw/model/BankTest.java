package com.miw.model;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class BankTest {

    @Test
    void getBankSinatra() {
        Bank bank = Bank.getBankSinatra();
        assertThat(bank).isNotNull();
    }

    @Test
    void getAccount() {
        assertThat(Bank.getBankSinatra().getAccount()).isNotNull();
    }

}