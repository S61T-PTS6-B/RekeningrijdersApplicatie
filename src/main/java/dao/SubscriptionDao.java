/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import model.Account;
import model.RoadSubscription;

/**
 *
 * @author Gijs
 */
@Stateless
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
    
    @Override
    public boolean RemoveSubscription(RoadSubscription s, int bsn){
        try{
            Account a = accountDao.FindAccount(bsn);
            s.setAccount(a);
            em.remove(s);
        }catch(Exception e){
            return false;
        }
        return true;
        
    }
    
    @Override
    public List<RoadSubscription> getSubscriptions(){
                Query query = em.createQuery(
                "SELECT r from RoadSubscription r");
        return (List<RoadSubscription>) query.getResultList();
    }
    
}
