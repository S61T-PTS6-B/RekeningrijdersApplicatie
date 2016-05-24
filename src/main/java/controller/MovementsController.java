/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.ManagedBean;
import model.Movement;

/**
 *
 * @author Gijs
 */
@Named(value = "movements")
@ManagedBean
@SessionScoped
public class MovementsController implements Serializable {
    
    private List<Movement> movements;

    public List<Movement> getMovements() {       
        return movements;
    }

    public void setMovements(List<Movement> movements) {
        this.movements = movements;
    }
    
    public MovementsController() { 
        movements = new ArrayList<>();
        for (int i=1; i<10; i++) {
            Movement m = new Movement();
            double d = (double) i;
            m.setLatStart(50.123456 + (d/10));
            m.setLongStart(5.123456 + (d/10));
            m.setLatEnd(50.223456 + (d/10));
            m.setLongEnd(5.223456 + (d/10));
            m.setDate(new Date());
            movements.add(m);
        }
    }

    
}
