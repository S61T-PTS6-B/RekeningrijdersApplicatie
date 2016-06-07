/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.io.Serializable;
import java.net.ConnectException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import model.Invoice;
import model.Movement;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import service.IRekeningrijderService;
import utilities.AESEncrypt;

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
        Invoice bill = new Invoice();
        bill.setAmount(34.0);
        bill.setKilometers(257);
        bill.setMonth(1);
        bill.setYear(2016);
        bill.setPaid(true);
        
        Invoice bill2 = new Invoice();
        bill2.setAmount(45.0);
        bill2.setKilometers(311);
        bill2.setMonth(2);
        bill2.setYear(2016);
        bill2.setPaid(false);
        
        Invoice bill3 = new Invoice();
        bill3.setAmount(63.0);
        bill3.setKilometers(454);
        bill3.setMonth(5);
        bill3.setYear(2016);
        bill3.setPaid(false);
        
        bills.add(bill);
        bills.add(bill2);
        bills.add(bill3);
        billsMap.put(bill.getPeriod(), false);
        billsMap.put(bill2.getPeriod(), false);
        billsMap.put(bill3.getPeriod(), false);
    } 
    
    
    
    public void StartPaypalCheckout() { 
        message = "";
        billsToPay.clear();
        for (Invoice b : bills) {
            if (!b.getPaid() && billsMap.get(b.getPeriod())) {
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
        if (service.OnSuccessfulPayment()) {
            message = "";
            FacesContext.getCurrentInstance().getExternalContext().redirect("finishedpayment.xhtml");
        } else {
            message = "Uw betaling is mislukt.";
            FacesContext.getCurrentInstance().getExternalContext().redirect("billinginfo.xhtml");
        }
    } 
    
    public String GoToMovements() throws Exception {
        movements.clear();
        message = "";
        Client client = ClientBuilder.newClient();
        //String id = selectedBill.getLicensePlate();
        String id = "Cas van Gool";
        String send = "id=" + id  + "&month=" + selectedBill.getMonth() + "&year=" + selectedBill.getYear();
        String encrp = AESEncrypt.encrypt(send);
        try {
            WebTarget resource = client.target("http://145.93.81.14:8080/VerplaatsingSysteem/Rest/carTrackers/getMonth?code=" + encrp);
            String response = resource.request(MediaType.APPLICATION_JSON).get(String.class);
            JSONObject obj = new JSONObject(AESEncrypt.decrypt(response));
            JSONArray arr = obj.getJSONArray("locations");
            for (int i=0; i<arr.length(); i++) {
                JSONObject beginobj = arr.getJSONObject(i);
                JSONObject endobj;
                try {
                    endobj = arr.getJSONObject(i+1);
                } catch (JSONException ex) {
                    endobj = beginobj;
                }
                Movement m = new Movement();
                m.setLatStart(beginobj.getDouble("lat"));
                m.setLongStart(beginobj.getDouble("long"));
                m.setLatEnd(endobj.getDouble("lat"));
                m.setLongEnd(endobj.getDouble("long"));
                DateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");         
                m.setDate(format.parse(beginobj.getString("date")));
                movements.add(m);
            } 
        }
        catch (NotFoundException | ConnectException ex) {
            message = "De verplaatsingen konden niet worden opgehaald. Probeer het later nog eens.";
            return "billinginfo.xhtml";
        }
        return "movements.xhtml";
    }
}
