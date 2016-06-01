/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import com.paypal.exception.ClientActionRequiredException;
import com.paypal.exception.HttpErrorException;
import com.paypal.exception.InvalidCredentialException;
import com.paypal.exception.InvalidResponseDataException;
import com.paypal.exception.MissingCredentialException;
import com.paypal.exception.SSLConfigurationException;
import com.paypal.sdk.exceptions.OAuthException;
import controller.BillingController;
import dao.IAccountDao;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;
import model.Account;
import model.Invoice;
import org.xml.sax.SAXException;
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
import utilities.Mailing;
import utilities.PasswordStorage;

/**
 *
 * @author Gijs
 */
@Stateless(name="service")
public class RekeningrijderService implements IRekeningrijderService {    
    
    @EJB
    private IAccountDao accountDao;
    
    @Override
    public void setDao(IAccountDao dao) {
        this.accountDao = dao;
    }
    
    @Override
    public Account LogIn(int bsn, String password) 
    {
        if (accountDao.UserIsActivated(bsn)) {
            return accountDao.AuthenticateUser(bsn, password);
        }
        return null;
    }

    @Override
    public boolean Register(int bsn, String password, String email) {
        Account acc = new Account();
        if (email == null || email.equals("")) {
            //send letter
            return false;
        }
        else {          
            acc.setBsn(bsn);
            acc.setEmail(email);
            acc.setConfirmed(false);
            String uuid = java.util.UUID.randomUUID().toString();
            acc.setConfirmationId(uuid);
            HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            String fulllink = req.getRequestURL().toString();
            int endIndex = fulllink.lastIndexOf("/");
            if (endIndex != -1)  
            {
                fulllink = fulllink.substring(0, endIndex);
            }
            String link = fulllink + "/activateaccount?bsn=" + bsn + "&uuid=" + uuid; 
            if (accountDao.RegisterUser(acc)) {
                Mailing.SendEmail(email, "<p>Dank u voor het registreren voor De Rekeningrijder Online. Klik op onderstaande link om uw account te activeren: <br></br><br></br> <a>" + link + "</a></p>", "Activatie Rekeningrijder Online");
                try {
                acc.setPassword(PasswordStorage.createHash(password));
                } catch (PasswordStorage.CannotPerformOperationException ex) {
                    return false;
                }
                return true;
            }
            return false;
        }
    }   

    @Override
    public boolean ActivateUser(String bsn, String uuid) {
        int bsnint = Integer.parseInt(bsn);
        Account acc = accountDao.FindAccount(bsnint);
        if (acc.getConfirmationId() == null ? uuid == null : acc.getConfirmationId().equals(uuid)) {
            accountDao.ActivateUser(acc);
            return true;
        }
        return false;
    }

    @Override
    public void DoPayPalCheckout(List<Invoice> invoices) {
        PaymentDetailsType paymentDetails = new PaymentDetailsType();
        paymentDetails.setPaymentAction(PaymentActionCodeType.fromValue("Sale"));
        List<PaymentDetailsItemType> lineItems = new ArrayList<>();
        double totalAmount = 0;
        for (Invoice b : invoices) {
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

    @Override
    public boolean OnSuccessfulPayment() {
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
                //Send paid message to OverheidsApplicatie
                return true;
            } 
            
        } catch (SSLConfigurationException | InvalidCredentialException | IOException | HttpErrorException | InvalidResponseDataException | ClientActionRequiredException | MissingCredentialException | InterruptedException | OAuthException | ParserConfigurationException | SAXException ex) {
            Logger.getLogger(BillingController.class.getName()).log(Level.SEVERE, null, ex);
        } 
        return false;
    }
}
