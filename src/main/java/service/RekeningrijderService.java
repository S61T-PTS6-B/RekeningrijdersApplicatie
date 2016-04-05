/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import dao.IAccountDao;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import model.Account;

/**
 *
 * @author Gijs
 */
@Stateless
public class RekeningrijderService implements IRekeningrijderService {    
    
    @EJB
    private IAccountDao accountDao;
    
    @Override
    public Account LogIn(int bsn, String password) 
    {
        return accountDao.AuthenticateUser(bsn, password);
    }

    @Override
    public boolean Register(int bsn, String password) {
        return false;
    }
    
    
    
}
