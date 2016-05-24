/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Date;

/**
 *
 * @author Gijs
 */
public class Movement {
    
    private double latStart;
    private double longStart;
    private double latEnd;
    private double longEnd;
    private Date date; 

    public Movement(double latStart, double longStart, double latEnd, double longEnd, Date date) {
        this.latStart = latStart;
        this.longStart = longStart;
        this.latEnd = latEnd;
        this.longEnd = longEnd;
        this.date = date;
    }
    
    public Movement() {}
    
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getLatStart() {
        return latStart;
    }

    public void setLatStart(double latStart) {
        this.latStart = latStart;
    }

    public double getLongStart() {
        return longStart;
    }

    public void setLongStart(double longStart) {
        this.longStart = longStart;
    }

    public double getLatEnd() {
        return latEnd;
    }

    public void setLatEnd(double latEnd) {
        this.latEnd = latEnd;
    }

    public double getLongEnd() {
        return longEnd;
    }

    public void setLongEnd(double longEnd) {
        this.longEnd = longEnd;
    }

    
}
