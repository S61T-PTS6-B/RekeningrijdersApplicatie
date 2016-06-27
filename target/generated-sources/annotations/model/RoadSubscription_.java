package model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import model.Account;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-06-27T09:04:23")
@StaticMetamodel(RoadSubscription.class)
public class RoadSubscription_ { 

    public static volatile SingularAttribute<RoadSubscription, Long> id;
    public static volatile SingularAttribute<RoadSubscription, Account> account;
    public static volatile SingularAttribute<RoadSubscription, String> roadname;

}