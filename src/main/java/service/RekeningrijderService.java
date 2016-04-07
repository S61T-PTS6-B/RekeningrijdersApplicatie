/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import dao.IAccountDao;
import dao.IExternalCommunication;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import model.Account;
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
    
    @EJB
    private IExternalCommunication external;
    
    @Override
    public Account LogIn(int bsn, String password) 
    {
        if (accountDao.UserIsActivated(bsn)) {
            return accountDao.AuthenticateUser(bsn, password);
        }
        return null;
    }

    @Override
    public boolean Register(int bsn, String password) {
        Account acc = new Account();
        String email = external.GetEmailFromBsn(bsn);
        if (email == null) {
            //send letter
        }
        else {          
            acc.setBsn(bsn);
            acc.setEmail(email);
            acc.setConfirmed(false);
            String uuid = java.util.UUID.randomUUID().toString();
            acc.setConfirmationId(uuid);
            String link = "http://localhost:8080/RekeningrijdersApplicatie/activateaccount?bsn=" + bsn + "&uuid=" + uuid;
            
            Mailing.SendEmail(email, "Dank u voor het registreren voor De Rekeningrijder Online. Klik op de volgende link om uw account te activeren: " + link, "Activatie Rekeningrijder Online");
            try {
                acc.setPassword(PasswordStorage.createHash(password));
            } catch (PasswordStorage.CannotPerformOperationException ex) {
                return false;
            }           
        }
        return accountDao.RegisterUser(acc);
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
}
