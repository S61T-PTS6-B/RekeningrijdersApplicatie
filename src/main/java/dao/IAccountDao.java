/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import model.Account;

/**
 *
 * @author Gijs
 */
public interface IAccountDao {
        
    Account AuthenticateUser(int bsn, String password);
    
    boolean RegisterUser(int bsn, String password);
}
