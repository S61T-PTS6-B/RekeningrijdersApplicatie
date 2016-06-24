/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import model.RoadSubscription;

/**
 *
 * @author Gijs
 */
public interface ISubscriptionDao {
    void SaveSubscription(RoadSubscription s, int bsn);
}
