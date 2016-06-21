/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package messaging;

import java.util.Arrays;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import model.Account;
import model.Invoice;
import service.IRekeningrijderService;

/**
 *
 * @author Gijs
 */
@MessageDriven(mappedName = "InvoiceQueue", activationConfig = {
    @ActivationConfigProperty(propertyName = "acknowledgeMode",
            propertyValue = "Auto-acknowledge"),
    @ActivationConfigProperty(propertyName = "destinationType",
            propertyValue = "javax.jms.Queue")
})
public class InvoiceSaver implements MessageListener {
    
    public InvoiceSaver() {
    }
    
    @EJB
    private IRekeningrijderService service;
    
    @Override
    public void onMessage(Message message) {
        try {
            String bsn = message.getStringProperty("bsn");
            Long id = message.getLongProperty("id");
            String licensePlate = message.getStringProperty("cartracker");
            double totalAmount = message.getDoubleProperty("totalAmount");
            int month = message.getIntProperty("month");
            int year = message.getIntProperty("year");
            boolean paid = message.getBooleanProperty("paid");
            String urlToDownload = message.getStringProperty("URLToDownload");
            double totalDistance = message.getDoubleProperty("totalDistance");
            Invoice i = new Invoice();
            i.setId(id);
            i.setKilometers(totalDistance);
            i.setLicensePlate(licensePlate);
            i.setMonth(month);
            i.setYear(year);
            i.setPaid(paid);
            Account a = service.GetAccount(Integer.parseInt(bsn));           
            if (a == null) {
                InvoiceUpdater.sendPackage(id, "paid");               
            } 
            a = service.GetAccount(218180901);
            i.setOwner(a);
            service.SaveInvoice(i);
        } catch (JMSException ex) {
            System.out.println(Arrays.toString(ex.getStackTrace()) + "ERROR!");
        }
    }
    
}
