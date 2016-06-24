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
import javax.inject.Inject;
import service.IRekeningrijderService;

/**
 *
 * @author Gijs
 */
@Named(value = "roadSubscriptionController")
@ManagedBean
@SessionScoped
public class RoadSubscriptionController implements Serializable {
   
    private String roadName;
    private String message;
    @Inject
    private AccountController accountController;
    @EJB
    private IRekeningrijderService service;
    
    public RoadSubscriptionController() {}

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRoadName() {
        return roadName;
    }

    public void setRoadName(String roadName) {
        this.roadName = roadName;
    }
    
    public void SubscribeToRoad() {
        if (roadName.isEmpty()) {
            message = "Vul een weg in.";
            return;
        }
        int bsn = Integer.parseInt(accountController.getBsn());
        service.SaveSubscription(roadName, bsn);
        message = "Wijzigingen opgeslagen. U krijgt een mail wanneer er een file staat op de " + roadName;
        roadName = "";
    }
}
