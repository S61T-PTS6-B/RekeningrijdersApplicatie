/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.ManagedBean;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import model.Account;
import org.json.JSONException;
import org.json.JSONObject;
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
    
    public void CheckForActiveSession() {
        try {
            if (activeUser == null) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
            }
        } catch (Exception ex) {
            //swallow
        }
    }
    
    public void SimulateError() throws JSONException {
        try {
            URL url = new URL("http://145.93.165.43:9233/MonitoringSysteem/Rest/statusmessages/postmessage");           
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            OutputStream os = conn.getOutputStream();
            JSONObject obj = new JSONObject();
            obj.put("systeemnaam", "RekeningrijdersApplicatie");
            obj.put("message","Down for maintenance");
            String input = obj.toString();
            os.write(input.getBytes());
            os.flush();
            BufferedReader br = new BufferedReader (new InputStreamReader((conn.getInputStream()))); 
            String output;
            System.out.println("Response from server:\n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }
            conn.disconnect();
 
        } catch (IOException ex) {
            Logger.getLogger(AccountController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
