/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

/**
 *
 * @author Gijs
 */
public interface IExternalCommunication {
    
    /**
     * Gets the email thats belongs to this bsn from the government application
     * @param bsn
     * @return the corresponding email. If no email is not known, return empty string.
     */
    public String GetEmailFromBsn(int bsn);
}
