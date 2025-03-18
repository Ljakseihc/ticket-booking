package com.epam.nosql.search.service;

import com.epam.nosql.search.dto.UserAccount;

import java.math.BigDecimal;

public interface UserAccountService {

    UserAccount refillAccount(long userId, BigDecimal money);
}
