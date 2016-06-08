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

/**
 *
 * @author Gijs
 */
@Stateless
public class TestDao implements ITestDao {
    
    @PersistenceContext(name="RekeningrijdersApplicatiePU")
    private EntityManager em;
    
    @Override
    public boolean DatabaseOnline() {
        try {
        int count = (int) em.createQuery("SELECT COUNT(a) FROM Account a").getSingleResult();
        } catch (Exception ex) {
            return false;
        }
        return true;
    }
}
