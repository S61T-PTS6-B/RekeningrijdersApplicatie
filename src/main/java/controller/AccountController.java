/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.ManagedBean;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import model.Account;
import model.BillingDummy;
import service.IRekeningrijderService;
/**
 *
 * @author Gijs
 */
@Named(value = "accountController")
@ManagedBean
@SessionScoped
public class AccountController implements Serializable {
    
    @EJB
    private IRekeningrijderService service;
    
    private Account activeUser;
    private String bsn; 
    private String password;
    private String message;

    public AccountController() {}
    
    public String getBsn() {
        return bsn;
    }
    
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setBsn(String bsn) {
        this.bsn = bsn;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Account getActiveUser() {
        return activeUser;
    }

    public void setActiveUser(Account activeUser) {
        this.activeUser = activeUser;
    }    
    
    public String logIn() {
        int bsnint = Integer.parseInt(bsn);
        this.activeUser = service.LogIn(bsnint, password);
        if (this.activeUser == null) {
            message = "Inloggen mislukt. Controleer of uw BSN en wachtwoord correct ingevuld is en of uw account geactiveerd is.";
            return "login.xhtml";
        } else {
            message = "";
            return "main.xhtml";
        }
    }   
    
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
