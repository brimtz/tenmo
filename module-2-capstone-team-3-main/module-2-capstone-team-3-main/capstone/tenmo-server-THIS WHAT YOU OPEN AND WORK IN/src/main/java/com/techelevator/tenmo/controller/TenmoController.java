package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.datasource.JdbcDao.JdbcTenmoAccountDao;
import com.techelevator.tenmo.datasource.JdbcDao.JdbcTenmoTransferDao;
import com.techelevator.tenmo.datasource.dao.TenmoAccountDao;
import com.techelevator.tenmo.datasource.dao.TenmoTransferDao;
import com.techelevator.tenmo.datasource.model.TenmoAccount;
import com.techelevator.tenmo.datasource.model.TenmoTransfer;
import com.techelevator.tenmo.usermanagement.model.User;
import com.techelevator.tenmo.usermanagement.model.dao.UserDao;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@RestController
@RequestMapping//("/tenmo")
public class TenmoController {

        private TenmoAccountDao     tenmoAccount;
        private TenmoTransferDao    tenmoTransfer;

        private UserDao user;

        public TenmoController(TenmoAccountDao tenmoAccountDao, TenmoTransferDao tenmoTransferDao, UserDao userDao) {
          this.tenmoAccount = tenmoAccountDao;
          this.tenmoTransfer = tenmoTransferDao;
          this.user = userDao;
        };
//
                /**
                 * Handles an HTTP POST request for the path: /account
                 * <p>
                 * Add a TenmoAccount object to  the datasource
                 *
                 * @param aNewAccount - must be present in the request body as a valid JSON object for a TenmoAccount object
                 *                    Note: If an account is sent in the JSON object it will be ignored as the data source
                 *                    manager will assign a unique account id when storing the TenmoObject
                 * @return the TenmoAccount object with the data source assigned accountId
                 */

                // TODO: Write a controller to handle an HTTP POST request for the path: /account
                @RequestMapping(path = "/account", method = RequestMethod.POST)
                public TenmoAccount createAnAccount(@RequestBody TenmoAccount aNewAccount) {
                    return tenmoAccount.saveAccount(aNewAccount);
                }


                /**
                 * Handle an HTTP GET request for the path: /account/{accountId}
                 * <p>
                 * Return the account from the data source with the accountId provided
                 * <p>
                 * Note: The accountId requested must be specified as a path variable in the request
                 *
                 * @param theAcctId - must be specified as a path variable
                 * @return the TenmoAccount object for the accountId specified or null
                 */

                // TODO: Write a controller to handle an HTTP GET request for the path: /account/{accountId}
                @RequestMapping(path = "/account/{theAcctId}", method = RequestMethod.GET)
                // @PathVariable in the method parameter list says go get the path variable and store in the Java variable
                // Typically the path variable and Java have to same name
                public TenmoAccount get(@PathVariable long theAcctId) { // go get the path variable called {id} and store it int id
                    return tenmoAccount.getAccountForAccountId(theAcctId); // call teh DAO to get a hotel by id and return it
                }

                /**
                 * Handle an HTTP GET request for either the path: /account
                 * or: /account?id=userId
                 * <p>
                 * if the /account path is used for the request, all TenmoAccounts in the datasource will be returned
                 * <p>
                 * if the /account?id=usedId path is used in the request, all accounts for the specified userid will be returned
                 *
                 * @param theUserId - optional query parameter to request all accounts for a specific userid
                 * @return - a list containing all accounts indicated by the path or an empty list if no accounts found
                 */
                // TODO: Write a controller to handle a GET request for either the path: /account
                //                                                                   or: /account?id=userId

                @RequestMapping(path = "/account", method = RequestMethod.GET)
                // @PathVariable in the method parameter list says go get the path variable and store in the Java variable
                // Typically the path variable and Java have to same name
                public List<TenmoAccount> get(@RequestParam (name="userid", required = false, defaultValue = "0") int theUserId) { // go get the path variable called {id} and store it int id
                    if (theUserId != 0) {
                        return tenmoAccount.getAccountsForAUserId(theUserId);

                    } else {
                        return tenmoAccount.getAllAccounts();

                    }
                    //String.valueOf(theUserId) != null     call teh DAO to get a hotel by id and return it
                }

                /**
                 * Handles an HTTP GET request for path /user
                 *
                 * @return - a list of all users in the data source
                 */

                // TODO: Write a controller to handle a  GET request for path /user

                @RequestMapping(path = "/user", method = RequestMethod.GET)
                public List<User> anyNameYouWant() {
                    try {
                        return user.findAll();
                    } catch (NullPointerException e){
                        throw new NullPointerException("User is null");
                    }

                }
                /**
                 * Handles an HTTP PUT request for the path: /account
                 *
                 * Add a TenmoAccount object to  the datasource
                 *
                 * @param theUpdatedAcct - must be present in the request body as a valid JSON object for a TenmoAccount object
                 *
                 *
                 * @return the update TenmoAccount object from the datasource
                 */

                // TODO: Write a controller to handle an HTTP PUT request for the path: /account
                @RequestMapping(path = "/account", method = RequestMethod.PUT)
                public TenmoAccount update(@RequestBody TenmoAccount theUpdatedAcct) {

                        TenmoAccount updatedAccount = tenmoAccount.updateAccount(theUpdatedAcct);
                        return updatedAccount;
                }


                /**
                 * Handles an HTTP POST request for the path: /transfer
                 *
                 * Add a TenmoTransfer object to  the datasource
                 *
                 * @param theTransfer - must be present in the request body as a valid JSON object for a TenmoTransfer object
                 *
                 *
                 * @return the update TenmoTransfer object from the datasource
                 */

                // TODO: Write a controller to handle an HTTP POST request for the path: /transfer

                @RequestMapping(path = "/transfer", method = RequestMethod.POST)
                public TenmoTransfer transfer(@RequestBody TenmoTransfer theTransfer) {

                    try {
                        TenmoTransfer transferredAccount = tenmoTransfer.saveTransfer(theTransfer);
                        return transferredAccount;
                    } catch (NullPointerException e){
                        throw new NullPointerException("Transfer wasn't found");
                    }

                }

                /**
                 * Handles HTTP GET for path /transfer?id=userid
                 *
                 * Return all transfer for the userid given
                 *
                 * @param id - the userid whose transfers should be returned
                 */

                // TODO: Write a controller to handles HTTP GET for path /transfer?id=userid

                @RequestMapping(path = "/transfer", method = RequestMethod.GET)
                // @PathVariable in the method parameter list says go get the path variable and store in the Java variable
                // Typically the path variable and Java have to same name
                public List<TenmoTransfer> getTransfer(@RequestParam  int id) { // go get the path variable called {id} and store it int id
                        return tenmoTransfer.getTransfersForUser(id);

                }

                /**
                 * Helper method to log API calls made to the server
                 *
                 * @param message - message to be included in the server log
                 */
                public void logAPICall(String message) {
                    LocalDateTime now = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss.A");
                    String timeNow = now.format(formatter);
                    System.out.println(timeNow + "-" + message);
                }
            }