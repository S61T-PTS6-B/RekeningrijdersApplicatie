/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import model.Account;

/**
 *
 * @author Gijs
 */
public interface IRekeningrijderService 
{
    /**
     * Used to log in the user
     * @param bsn
     * @param password
     * @return the account object associated with the user. If authentication fails, null will be returned.
     */
    Account LogIn(int bsn, String password);
    
    /**
     * Used to register a new user
     * @param bsn
     * @param password
     * @return the account object associated with the new user.
     */
    boolean Register(int bsn, String password);
}
