/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import model.Movement;

/**
 *
 * @author Gijs
 */
public interface IMovementsDao {
    List<Movement> GetMovements(String licensePlate, int month, int year) throws Exception;
}
