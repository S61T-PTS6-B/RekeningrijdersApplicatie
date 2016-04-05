/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import model.Account;
import utilities.PasswordStorage;

/**
 *
 * @author Gijs
 */
@Stateless
public class AccountDao implements IAccountDao {

    @PersistenceContext(name="RekeningrijdersApplicatiePU")
    private EntityManager em;
    
    @Override
    public Account AuthenticateUser(int bsn, String password)
    {
        Account acc = em.find(Account.class, bsn);
        try {
            if (acc != null && PasswordStorage.verifyPassword(password, acc.getPassword()))
            {
                return acc;       
            }
        } catch (PasswordStorage.CannotPerformOperationException | PasswordStorage.InvalidHashException ex) {
            return null;
        }
        return null;
    }    

    @Override
    public boolean RegisterUser(int bsn, String password) {
        return false;
    }
}
