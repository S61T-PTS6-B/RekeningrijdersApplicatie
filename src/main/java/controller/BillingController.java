/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.ManagedBean;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.bean.SessionScoped;
import model.BillingDummy;
import service.IRekeningrijderService;

/**
 *
 * @author Gijs
 */
@Named(value = "billingController")
@ManagedBean
@SessionScoped
public class BillingController {
    
    @EJB
    private IRekeningrijderService service;
    
    public BillingController() {}

    public List<BillingDummy> GetBillsList() {
        List<BillingDummy> bills = new ArrayList<>();
        
        BillingDummy bill = new BillingDummy();
        bill.setAmount(34);
        bill.setKilometers(257);
        bill.setPeriod("01-01-2016 - 01-02-2016");
        bill.setPaid("Ja");
        
        BillingDummy bill2 = new BillingDummy();
        bill2.setAmount(45);
        bill2.setKilometers(311);
        bill2.setPeriod("01-02-2016 - 01-03-2016");
        bill2.setPaid("Ja");
        
        BillingDummy bill3 = new BillingDummy();
        bill3.setAmount(45);
        bill3.setKilometers(311);
        bill3.setPeriod("01-02-2016 - 01-03-2016");
        bill3.setPaid("Nee");
        
        bills.add(bill);
        bills.add(bill2);
        bills.add(bill3);
        
        return bills;
    }    
}
