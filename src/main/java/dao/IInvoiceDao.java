/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import model.Invoice;

/**
 *
 * @author Gijs
 */
public interface IInvoiceDao {
    /**
     * 
     * @param invoiceId 
     */
    void SetInvoicePaid(Long invoiceId);
    
    /**
     * 
     * @param i 
     */
    void SaveInvoice(Invoice i);
    
    /**
     * 
     * @param bsn
     * @return 
     */
    List<Invoice> GetInvoicesFromUser(int bsn);
}
