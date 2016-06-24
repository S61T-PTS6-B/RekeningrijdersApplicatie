/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import model.Account;
import model.RoadSubscription;

/**
 *
 * @author Gijs
 */
public class SubscriptionDao implements ISubscriptionDao {

    @EJB
    private IAccountDao accountDao;
    @PersistenceContext(name="RekeningrijdersApplicatiePU")
    private EntityManager em;
    
    @Override
    public void SaveSubscription(RoadSubscription s, int bsn) {
        Account a = accountDao.FindAccount(bsn);
        s.setAccount(a);
        em.persist(s);
    }
    
}
