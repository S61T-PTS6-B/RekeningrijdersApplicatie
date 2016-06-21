/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import model.Invoice;
import model.Movement;
import service.IRekeningrijderService;

/**
 *
 * @author Gijs
 */
@Named(value = "billingController")
@ManagedBean
@SessionScoped
public class BillingController implements Serializable {
    
    @EJB
    private IRekeningrijderService service; 
    
    @Inject
    private AccountController accountController;
    
    private String message;
    private Invoice selectedBill;
    private List<Invoice> bills = new ArrayList<>();
    private final List<Invoice> billsToPay = new ArrayList<>();
    private Map<String, Boolean> billsMap = new HashMap<>();
    private List<Movement> movements = new ArrayList<>();
 
    public List<Movement> getMovements() {
        return movements;
    }

    public void setMovements(List<Movement> movements) {
        this.movements = movements;
    }
    
    public Invoice getSelectedBill() {
        return selectedBill;
    }

    public void setSelectedBill(Invoice selectedBill) {
        this.selectedBill = selectedBill;
    }
    
    public Map<String, Boolean> getBillsMap() {
        return billsMap;
    }

    public void setBillsMap(Map<String, Boolean> billsMap) {
        this.billsMap = billsMap;
    }

    public List<Invoice> getBills() {
        return bills;
    }

    public void setBills(List<Invoice> bills) {
        this.bills = bills;
    }
    
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    public BillingController() {} 

    @PostConstruct
    public void FillBillsList() {
        bills.clear();
        billsToPay.clear();
        String bsn = accountController.getBsn();
        List<Invoice> invoices = service.GetInvoicesFromUser(Integer.parseInt(bsn));
        for (Invoice i : invoices) {
            bills.add(i);           
            billsMap.put(i.getCharacteristic(), i.getPaid());
        }      
    } 
    
    
    
    public void StartPaypalCheckout() { 
        message = "";
        billsToPay.clear();
        for (Invoice b : bills) {
            if (!b.getPaid() && billsMap.get(b.getCharacteristic())) {
                billsToPay.add(b);
            }
        }
        if (billsToPay.isEmpty()) {
            message = "Selecteer minimaal één rekening die u wilt betalen.";
            return;
        }
        service.DoPayPalCheckout(billsToPay);      
    }
    
    public void OnSuccessfulPayment() throws IOException {
        if (service.OnSuccessfulPayment(billsToPay)) {
            message = ""; 
            FillBillsList();
            FacesContext.getCurrentInstance().getExternalContext().redirect("finishedpayment.xhtml");
        } else {
            message = "Uw betaling is mislukt.";
            FacesContext.getCurrentInstance().getExternalContext().redirect("billinginfo.xhtml");
        }
    } 
    
    public String GoToMovements() throws Exception {
        movements.clear();
        message = "";
        movements = service.GetMovements(selectedBill.getLicensePlate(), selectedBill.getMonth(), selectedBill.getYear());
        if (movements == null || movements.isEmpty()) {
            message = "De verplaatsingen konden niet worden opgehaald. Probeer het later nog eens.";
            return "billinginfo.xhtml";
        }       
        return "movements.xhtml";
    }
}
