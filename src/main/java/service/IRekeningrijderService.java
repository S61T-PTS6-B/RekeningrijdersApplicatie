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
import model.Movement;

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
     * @param invoices
     * @return true when payment successful, false otherwise.
     */
    boolean OnSuccessfulPayment(List<Invoice> invoices);
    
    /**
     * 
     * @param licensePlate
     * @param month
     * @param year
     * @return the movements belonging to the licensePlate and the given period.
     */
    List<Movement> GetMovements(String licensePlate, int month, int year);
    
    /**
     * 
     * @return true if the database is reachable and responding, false otherwise.
     */
    boolean DatabaseIsOnline();
    
    /**
     * Sets the invoice with given id to paid, in own database as well as government database (via JMS)
     * @param invoiceId 
     */
    void SetInvoicePaid(Long invoiceId);
    
    /**
     * Saves an invoice to the local database
     * @param i 
     */
    void SaveInvoice(Invoice i);
    
    /**
     * Gets the invoices belonging to a user
     * @param bsn
     * @return 
     */
    List<Invoice> GetInvoicesFromUser(int bsn);
    
    /**
     * Gets the account with specified bsn.
     * @param bsn
     * @return 
     */
    Account GetAccount(int bsn);
    
    /**
     * 
     * @param roadname
     * @param bsn 
     */
    void SaveSubscription(String roadname, int bsn);
}
