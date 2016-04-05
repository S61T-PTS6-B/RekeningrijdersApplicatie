/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author Gijs
 */
@Entity
public class Account implements Serializable {

    @Id
    private int bsn;
    private String password;

    public int getBsn() {
        return bsn;
    }

    public void setEmailAddress(int bsn) {
        this.bsn = bsn;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }  
}
