package com.techelevator.tenmo.datasource.JdbcDao;

import com.techelevator.tenmo.datasource.dao.TenmoAccountDao;
import com.techelevator.tenmo.datasource.dao.TenmoTransferDao;
import com.techelevator.tenmo.datasource.model.TenmoAccount;
import com.techelevator.tenmo.datasource.model.TenmoTransfer;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class JdbcTenmoTransferDao implements TenmoTransferDao {

    private JdbcTemplate jdbcTemplate;
    private TenmoAccountDao accountDao;

    public JdbcTenmoTransferDao(JdbcTemplate jdbcTemplate, TenmoAccountDao accountDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.accountDao =  accountDao;
    }
    @Override
    public TenmoTransfer saveTransfer(TenmoTransfer aTransfer) {

        // Done: Given a Transfer object write a method to get add the Transfer to the data source
        TenmoTransfer savedTransfer = null;
        // column names in table are different from TenmoTransfer instance variable names
        String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                     "VALUES (?, ?, ?, ?, ?) RETURNING transfer_id";

        // CHECK WITH FRANK TO MAKE SURE LOGIC / IMPLEMENTATION IS CORRECT
        // use .ordinal() for the enum types (transferStatus, transferType)
                // the transfer table stores the numeric values of these constants
        // use .getAccount_id() for account_from & account_to bc their get methods return a TenmoAccount
                // not the int of their account_id, which is what the transfer table stores
        long savedTransferId = jdbcTemplate.queryForObject(sql, long.class,
                aTransfer.getTransferStatus().ordinal(), aTransfer.getTransferType().ordinal(),
                aTransfer.getFromTenmoAccount().getAccount_id(),
                aTransfer.getToTenmoAccount().getAccount_id(),
                aTransfer.getAmount());
        savedTransfer = getATransferById(savedTransferId);
        return savedTransfer;
    }

    @Override
    public List<TenmoTransfer> getTransfersForUser(int userId) {

        // Done: Given a user id, write a method to retrieve all transfers for that user id from the data source
        List<TenmoTransfer> allTransfers = new ArrayList<>();
        String sql = "SELECT * FROM transfer JOIN account ON transfer.account_from = account.account_id " +
                     "WHERE account.user_id = ?";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        while (results.next()) {
            allTransfers.add(mapRowToTransfer(results));
        }
        return allTransfers;
    }


    @Override
    public TenmoTransfer getATransferById(Long transferIdRequested) {

        // Done: Given a transfer id, retrieve the Transfer from the data source

        String sql = "SELECT * FROM transfer WHERE transfer_id = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, transferIdRequested);
        if (result.next()) {
            return mapRowToTransfer(result);
        }
        return null;
    }

    /**
     *     private Long            transferId;
     *     private TRANSFER_STATUS transferStatus;
     *     private TRANSFER_TYPE   transferType;
     *     private TenmoAccount    fromTenmoAccount;
     *     private TenmoAccount    toTenmoAccount;
     *     private BigDecimal      amount;
     */

    // CHECK LOGIC WITH FRANK
    private TenmoTransfer mapRowToTransfer(SqlRowSet results) {
        TenmoTransfer transfer = new TenmoTransfer();
        transfer.setTransferId(results.getLong("transfer_id"));

        // NOTE: transferStatus & transferType are enums but they are ints in the database
        transfer.setTransferStatus(TenmoTransfer.TRANSFER_STATUS.values()[results.getInt("transfer_status_id")]);
        transfer.setTransferType(TenmoTransfer.TRANSFER_TYPE.values()[results.getInt("transfer_type_id")]);

        // NOTE: fromTenmoAccount & toTenmoAccount are TenmoAccount objects but int in the database
        long accountFromId = results.getLong("account_from");
        transfer.setFromTenmoAccount(accountDao.getAccountForAccountId(accountFromId));

        long accountToId = results.getLong("account_to");
        transfer.setToTenmoAccount(accountDao.getAccountForAccountId(accountToId));

        transfer.setAmount(results.getBigDecimal("amount"));
        return transfer;
    }

}
