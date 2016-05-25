/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.paypal.exception.ClientActionRequiredException;
import com.paypal.exception.HttpErrorException;
import com.paypal.exception.InvalidCredentialException;
import com.paypal.exception.InvalidResponseDataException;
import com.paypal.exception.MissingCredentialException;
import com.paypal.exception.SSLConfigurationException;
import com.paypal.sdk.exceptions.OAuthException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;
import model.BillingDummy;
import org.xml.sax.SAXException;
import service.IRekeningrijderService;
import urn.ebay.api.PayPalAPI.DoExpressCheckoutPaymentReq;
import urn.ebay.api.PayPalAPI.DoExpressCheckoutPaymentRequestType;
import urn.ebay.api.PayPalAPI.DoExpressCheckoutPaymentResponseType;
import urn.ebay.api.PayPalAPI.GetExpressCheckoutDetailsReq;
import urn.ebay.api.PayPalAPI.GetExpressCheckoutDetailsRequestType;
import urn.ebay.api.PayPalAPI.GetExpressCheckoutDetailsResponseType;
import urn.ebay.api.PayPalAPI.PayPalAPIInterfaceServiceService;
import urn.ebay.api.PayPalAPI.SetExpressCheckoutReq;
import urn.ebay.api.PayPalAPI.SetExpressCheckoutRequestType;
import urn.ebay.api.PayPalAPI.SetExpressCheckoutResponseType;
import urn.ebay.apis.CoreComponentTypes.BasicAmountType;
import urn.ebay.apis.eBLBaseComponents.AckCodeType;
import urn.ebay.apis.eBLBaseComponents.CurrencyCodeType;
import urn.ebay.apis.eBLBaseComponents.DoExpressCheckoutPaymentRequestDetailsType;
import urn.ebay.apis.eBLBaseComponents.PaymentActionCodeType;
import urn.ebay.apis.eBLBaseComponents.PaymentDetailsItemType;
import urn.ebay.apis.eBLBaseComponents.PaymentDetailsType;
import urn.ebay.apis.eBLBaseComponents.SetExpressCheckoutRequestDetailsType;

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
    private List<BillingDummy> bills = new ArrayList<>();
    private List<BillingDummy> billsToPay = new ArrayList<>();
    private Map<String, Boolean> billsMap = new HashMap<>();

    public Map<String, Boolean> getBillsMap() {
        return billsMap;
    }

    public void setBillsMap(Map<String, Boolean> billsMap) {
        this.billsMap = billsMap;
    }

    public List<BillingDummy> getBills() {
        return bills;
    }

    public void setBills(List<BillingDummy> bills) {
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
        BillingDummy bill = new BillingDummy();
        bill.setAmount(34.0);
        bill.setKilometers(257);
        bill.setPeriod("01-01-2016 - 01-02-2016");
        bill.setPaid(true);
        
        BillingDummy bill2 = new BillingDummy();
        bill2.setAmount(45.0);
        bill2.setKilometers(311);
        bill2.setPeriod("01-02-2016 - 01-03-2016");
        bill2.setPaid(false);
        
        BillingDummy bill3 = new BillingDummy();
        bill3.setAmount(63.0);
        bill3.setKilometers(454);
        bill3.setPeriod("01-03-2016 - 01-04-2016");
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
        for (BillingDummy b : bills) {
            if (!b.getPaid() && billsMap.get(b.getPeriod())) {
                billsToPay.add(b);
            }
        }
        if (billsToPay.isEmpty()) {
            message = "Selecteer minimaal één rekening die u wilt betalen.";
            return;
        }
        PaymentDetailsType paymentDetails = new PaymentDetailsType();
        paymentDetails.setPaymentAction(PaymentActionCodeType.fromValue("Sale"));
        List<PaymentDetailsItemType> lineItems = new ArrayList<>();
        double totalAmount = 0;
        for (BillingDummy b : billsToPay) {
            PaymentDetailsItemType item = new PaymentDetailsItemType();
            BasicAmountType amt = new BasicAmountType();
            amt.setCurrencyID(CurrencyCodeType.EUR);            
            item.setQuantity(1);
            String amountstring = String.valueOf(b.getAmount());
            amt.setValue(amountstring);  
            item.setName("Rekeningrijden " + b.getPeriod());
            item.setAmount(amt);
            lineItems.add(item);
            totalAmount += b.getAmount();
        }
        paymentDetails.setPaymentDetailsItem(lineItems);
        BasicAmountType orderTotal = new BasicAmountType();
        orderTotal.setCurrencyID(CurrencyCodeType.EUR);
        orderTotal.setValue(String.valueOf(totalAmount));
        paymentDetails.setOrderTotal(orderTotal);
        List<PaymentDetailsType> paymentDetailsList = new ArrayList<>();
        paymentDetailsList.add(paymentDetails);
        
        SetExpressCheckoutRequestDetailsType setExpressCheckoutRequestDetails = new SetExpressCheckoutRequestDetailsType();
        setExpressCheckoutRequestDetails.setReturnURL("http://localhost:8080/RekeningrijdersApplicatie/successfulpayment.xhtml");
        setExpressCheckoutRequestDetails.setCancelURL("http://localhost:8080/RekeningrijdersApplicatie/billinginfo.xhtml");
        
        setExpressCheckoutRequestDetails.setPaymentDetails(paymentDetailsList);
        
        SetExpressCheckoutRequestType setExpressCheckoutRequest = new SetExpressCheckoutRequestType(setExpressCheckoutRequestDetails);
        setExpressCheckoutRequest.setVersion("104.0");
        
        SetExpressCheckoutReq setExpressCheckoutReq = new SetExpressCheckoutReq();
        setExpressCheckoutReq.setSetExpressCheckoutRequest(setExpressCheckoutRequest);

        Map<String, String> sdkConfig = new HashMap<>();
        sdkConfig.put("mode", "sandbox");
        sdkConfig.put("acct1.UserName", "ptss61tb_api1.paypal.com");
        sdkConfig.put("acct1.Password", "6MXC4RDPRLE6L5SC");
        sdkConfig.put("acct1.Signature","AFcWxV21C7fd0v3bYYYRCpSSRl31AjRZwgZ-STUq7BR02xi9Y9pkQo6d");
        PayPalAPIInterfaceServiceService paypalservice = new PayPalAPIInterfaceServiceService(sdkConfig);
        try {
            SetExpressCheckoutResponseType setExpressCheckoutResponse = paypalservice.setExpressCheckout(setExpressCheckoutReq);
            String token = setExpressCheckoutResponse.getToken();           
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.redirect("https://www.sandbox.paypal.com/cgi-bin/webscr?cmd=_express-checkout&token=" + token);                 
        } catch (SSLConfigurationException | InvalidCredentialException | IOException | HttpErrorException | InvalidResponseDataException | ClientActionRequiredException | MissingCredentialException | InterruptedException | OAuthException | ParserConfigurationException | SAXException ex) {
            System.out.println("Error processing initial PayPal request: " + ex.toString() + "  -  " +  ex.getMessage());
        }
    }
    
    public void OnSuccessfulPayment() {
        HttpServletRequest request=(HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String token=request.getParameter("token");
        String payerId=request.getParameter("PayerID");
        
        GetExpressCheckoutDetailsRequestType getExpressCheckoutDetailsRequest = new GetExpressCheckoutDetailsRequestType(token);
        getExpressCheckoutDetailsRequest.setVersion("104.0");

        GetExpressCheckoutDetailsReq getExpressCheckoutDetailsReq = new GetExpressCheckoutDetailsReq();
        getExpressCheckoutDetailsReq.setGetExpressCheckoutDetailsRequest(getExpressCheckoutDetailsRequest);

        Map<String, String> sdkConfig = new HashMap<>();
        sdkConfig.put("mode", "sandbox");
        sdkConfig.put("acct1.UserName", "ptss61tb_api1.paypal.com");
        sdkConfig.put("acct1.Password", "6MXC4RDPRLE6L5SC");
        sdkConfig.put("acct1.Signature","AFcWxV21C7fd0v3bYYYRCpSSRl31AjRZwgZ-STUq7BR02xi9Y9pkQo6d");
        PayPalAPIInterfaceServiceService paypalservice = new PayPalAPIInterfaceServiceService(sdkConfig);
        try {
            GetExpressCheckoutDetailsResponseType response = paypalservice.getExpressCheckoutDetails(getExpressCheckoutDetailsReq);
            DoExpressCheckoutPaymentRequestDetailsType doExpressCheckoutPaymentRequestDetails = new DoExpressCheckoutPaymentRequestDetailsType();
            doExpressCheckoutPaymentRequestDetails.setToken(token);
            doExpressCheckoutPaymentRequestDetails.setPayerID(payerId);
            doExpressCheckoutPaymentRequestDetails.setPaymentDetails(response.getGetExpressCheckoutDetailsResponseDetails().getPaymentDetails());

            DoExpressCheckoutPaymentRequestType doExpressCheckoutPaymentRequest = new DoExpressCheckoutPaymentRequestType(doExpressCheckoutPaymentRequestDetails);
            doExpressCheckoutPaymentRequest.setVersion("104.0");

            DoExpressCheckoutPaymentReq doExpressCheckoutPaymentReq = new DoExpressCheckoutPaymentReq();
            doExpressCheckoutPaymentReq.setDoExpressCheckoutPaymentRequest(doExpressCheckoutPaymentRequest);
            
            DoExpressCheckoutPaymentResponseType finalresponse = paypalservice.doExpressCheckoutPayment(doExpressCheckoutPaymentReq); 
            
            if (finalresponse.getAck().equals(AckCodeType.SUCCESS)) {
                message = "";
                FacesContext.getCurrentInstance().getExternalContext().redirect("finishedpayment.xhtml");
                
                return;
            } 
            message = "De betaling kon niet worden uitgevoerd.";
            FacesContext.getCurrentInstance().getExternalContext().redirect("billinginfo.xhtml");
            
        } catch (SSLConfigurationException | InvalidCredentialException | IOException | HttpErrorException | InvalidResponseDataException | ClientActionRequiredException | MissingCredentialException | InterruptedException | OAuthException | ParserConfigurationException | SAXException ex) {
            Logger.getLogger(BillingController.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }   
}
