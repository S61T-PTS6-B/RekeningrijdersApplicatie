/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import model.Account;
import model.Invoice;

/**
 *
 * @author Gijs
 */
@Stateless
public class InvoiceDao implements IInvoiceDao {

    @PersistenceContext(name="RekeningrijdersApplicatiePU")
    private EntityManager em;
    
    @Override
    public void SetInvoicePaid(Long invoiceId) {
        Invoice i = em.find(Invoice.class, invoiceId);
        if (i != null) {
            i.setPaid(true);
            em.merge(i);
        }
    }   

    @Override
    public void SaveInvoice(Invoice i) {
        em.persist(i);
    }

    @Override
    public List<Invoice> GetInvoicesFromUser(int bsn) {
        Account a = em.find(Account.class, bsn);
        List<Invoice> invoices = a.getInvoices();
        if (invoices == null) {
            return new ArrayList<Invoice>();
        } 
        return invoices;
    }
}
