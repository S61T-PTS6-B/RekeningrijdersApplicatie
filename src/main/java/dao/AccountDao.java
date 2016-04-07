/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

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
    public boolean RegisterUser(Account user) {
        Account acc = em.find(Account.class, user.getBsn());
        if (acc == null) {
            em.persist(user);
            return true;
        }
        return false;
    }

    @Override
    public boolean UserIsActivated(int bsn) {
        Account acc = em.find(Account.class, bsn);
        if (acc == null) {
            return false;
        }
        return acc.isConfirmed();
    }

    @Override
    public void ActivateUser(Account user) {
        user.setConfirmed(true);
        em.merge(user);
    }

    @Override
    public Account FindAccount(int bsn) {
        return em.find(Account.class, bsn);
    }
}
