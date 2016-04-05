/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.Serializable;
import javax.annotation.ManagedBean;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import model.Account;
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
            message = "BSN of wachtwoord is incorrect";
            return "login.xhtml";
        } else {
            message = "";
            return "main.xhtml";
        }
    }   
}
