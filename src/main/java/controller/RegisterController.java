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
import service.IRekeningrijderService;

/**
 *
 * @author Gijs
 */
@Named(value = "registerController")
@ManagedBean
@SessionScoped
public class RegisterController implements Serializable {
    
    @EJB
    private IRekeningrijderService service;
    
    private String bsn;
    private String password1;
    private String password2;
    private String errormessage; 
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBsn() {
        return bsn;
    }

    public void setBsn(String bsn) {
        this.bsn = bsn;
    }
    
    public String getPassword1() {
        return password1;
    }

    public void setPassword1(String password1) {
        this.password1 = password1;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public String getErrormessage() {
        return errormessage;
    }

    public void setErrormessage(String errormessage) {
        this.errormessage = errormessage;
    }     
    
    public RegisterController() {}
    
    public String registerAccount() {
        if (password1 == null ? password2 != null : !password1.equals(password2)) { 
            errormessage = "Wachtwoorden komen niet overeen";
            return "register.xhtml";
        }
        int bsnint = Integer.parseInt(bsn);
        email = "gijshendrickx@hotmail.com";
        if (service.Register(bsnint, password1, email)) {
            return "successfulregistration.xhtml";
        } 
        errormessage = "Registratie mislukt. Mogelijk is uw BSN niet correct ingevuld of bestaat er al een account met dit BSN.";
        return "register.xhtml";
    }
}
