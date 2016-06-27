/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import model.RoadSubscription;

/**
 *
 * @author Gijs
 */
public interface ISubscriptionDao {
    void SaveSubscription(RoadSubscription s, int bsn);
    public boolean RemoveSubscription(RoadSubscription s, int bsn);
    public List<RoadSubscription> getSubscriptions();
}
