/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import javax.ejb.Stateless;

/**
 *
 * @author Gijs
 */
@Stateless
public class ExternalCommunication implements IExternalCommunication {

    @Override
    public String GetEmailFromBsn(int bsn) {
        return "gijshendrickx@hotmail.com";
    }
    
}
