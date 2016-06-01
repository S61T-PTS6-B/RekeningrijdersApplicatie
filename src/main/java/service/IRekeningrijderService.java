/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import dao.IAccountDao;
import java.util.List;
import model.Account;
import model.Invoice;

/**
 *
 * @author Gijs
 */
public interface IRekeningrijderService 
{
    /**
     * Set the data access object for the service
     * @param dao, the dao to be set
     */
    void setDao(IAccountDao dao);
    /**
     * Used to log in the user
     * @param bsn
     * @param password
     * @return the account object associated with the user. If authentication fails, null will be returned.
     */
    Account LogIn(int bsn, String password);
    
    /**
     * Used to register a new user
     * @param bsn
     * @param password
     * @param email
     * @return the account object associated with the new user.
     */
    boolean Register(int bsn, String password, String email);
    
    /**
     * Used to activate a user account. When activated, the account is usable.
     * @param bsn
     * @param uuid
     * @return true when activation is successful, false otherwise
     */
    boolean ActivateUser(String bsn, String uuid);
    
    /**
     * Used to initiate and complete a checkout with PayPal
     * @param invoices the invoices the user wants to pay
     */
    void DoPayPalCheckout(List<Invoice> invoices);
    
    /**
     * Gets called when the user has successfully paid on the PayPal site
     */
    boolean OnSuccessfulPayment();
}
