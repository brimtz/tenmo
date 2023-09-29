package com.techelevator.tenmo.datasource.dao;

import com.techelevator.tenmo.datasource.model.TenmoAccount;
import org.springframework.stereotype.Component;

import java.util.List;

public interface TenmoAccountDao {

    public List<TenmoAccount> getAllAccounts();

    public TenmoAccount getAccountForAccountId(Long theAccountId);

    public List<TenmoAccount> getAccountsForAUserId(int anUserId);

    public TenmoAccount saveAccount(TenmoAccount tenmoAccount2Save);

    public TenmoAccount updateAccount(TenmoAccount tenmoAccount2Update);


}
