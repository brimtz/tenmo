package com.techelevator.tenmo.services.interfaces;

import com.techelevator.tenmo.model.TenmoAccount;
import com.techelevator.tenmo.security.model.AuthenticatedUser;
import com.techelevator.tenmo.security.model.User;

import java.util.List;

public interface CurrencyTransferProcesses {
    /**
     * Get all the accounts from the data source
     *
     * @return - a list containing all the accounts in the data source - null if no accounts
     */
    List<TenmoAccount> getAllAccounts();

    /**
     * * Get all the accounts for a specific user id from the data source
     *
     * @param userId - the user id whose aIcounts you want retrieved
     *
     * @return - a list containing all the accounts in the data source for a specific userid - null if no accounts
     */
    List<TenmoAccount>  getAllAccountsForAUser(int userId);

    /**
     * Retrieve an account from the data source for a specific account_id
     *
     * @param accountId - the account id to be whose accounts should be retrieved
     *
     * @return - the account for the id specified or null if no account
     */
    public List<TenmoAccount>  getAnAccount(Long accountId);

    void viewCurrentBalance(AuthenticatedUser currentUser);

    User[] getUserForAccount(TenmoAccount anAccount);

    boolean sendBucks(AuthenticatedUser currentUser);

    void viewTransferHistory(AuthenticatedUser currentUser);

    void requestBucks(AuthenticatedUser currentUser);

    void viewPendingRequests(AuthenticatedUser currentUser);


}
