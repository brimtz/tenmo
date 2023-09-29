package com.techelevator.tenmo.datasource.JdbcDao;

import com.techelevator.tenmo.datasource.dao.TenmoAccountDao;
import com.techelevator.tenmo.datasource.model.TenmoAccount;
import com.techelevator.tenmo.datasource.model.TenmoTransfer;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTenmoAccountDao implements TenmoAccountDao {

    private final JdbcTemplate theDatabase;

    public JdbcTenmoAccountDao(JdbcTemplate jdbcTemplate) {
        this.theDatabase = jdbcTemplate;
    }

    @Override
    public List<TenmoAccount> getAllAccounts() {

        // DONE: Write a method to get all accounts from the data source
        List<TenmoAccount> accounts = new ArrayList<>();
        String sql = "SELECT account_id, user_id, balance from account;";
        SqlRowSet results = theDatabase.queryForRowSet(sql);

        while (results.next()) {
                accounts.add(mapRowToAccount(results));
        }
        return accounts;
    }


    @Override
    public List<TenmoAccount> getAccountsForAUserId(int theUserId) {

        // DONE: Write a method to get all accounts for a particular user id from the data source
        List<TenmoAccount> accounts = new ArrayList<>();
        String sql = "SELECT account_id, user_id, balance from account where user_id = ?;";
        SqlRowSet results = theDatabase.queryForRowSet(sql, theUserId);
        while (results.next()) {
            accounts.add(mapRowToAccount(results));
        }
        return accounts;
    }

    @Override
    public TenmoAccount getAccountForAccountId(Long theAccountId) {

        // DONE: Given an account id, write a method to get a specific account from the data source
        TenmoAccount account = null;
        String sql = "SELECT account_id, user_id, balance FROM account WHERE account_id = ?;";
        SqlRowSet results = theDatabase.queryForRowSet(sql, theAccountId);
        if (results.next()) {
            account = mapRowToAccount(results);
        }
        return account;
    }


    @Override
    public TenmoAccount saveAccount(TenmoAccount tenmoAccount2Save) {

        // DONE: Given an Account object write a method to get add an account to the data source (doesn't catch exceptions)
        TenmoAccount newAccount = null;

        String sql = "INSERT INTO account (user_id,balance) " +
                "VALUES (?,?) RETURNING account_id;";

        long newAccountId = theDatabase.queryForObject(sql, long.class,
                tenmoAccount2Save.getUser_id(), tenmoAccount2Save.getBalance());

        newAccount = getAccountForAccountId(newAccountId);

        return newAccount;
    }

    @Override
    public TenmoAccount updateAccount(TenmoAccount tenmoAccount2Update) {

        // Done: Given an Account object write a method to get update the account in the data source
        TenmoAccount updatedAccount = null;
        String sql = "UPDATE account SET user_id = ?, balance = ? " +
                     "WHERE account_id = ? RETURNING account_id";

        long updatedAccountId = theDatabase.queryForObject(sql, long.class,
                               tenmoAccount2Update.getUser_id(), tenmoAccount2Update.getBalance(),
                               tenmoAccount2Update.getAccount_id());
        updatedAccount = getAccountForAccountId(updatedAccountId);

        return updatedAccount;
    }



    private TenmoAccount mapRowToAccount(SqlRowSet rowSet) {
        TenmoAccount account = new TenmoAccount();

        account.setAccount_id(rowSet.getLong("account_id"));
        account.setUser_id(rowSet.getInt("user_id"));
        account.setBalance(rowSet.getBigDecimal("balance"));

        return account;
    }


}   // end of class
