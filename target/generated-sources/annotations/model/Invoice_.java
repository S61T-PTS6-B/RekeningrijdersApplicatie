package model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import model.Account;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-06-08T12:03:25")
@StaticMetamodel(Invoice.class)
public class Invoice_ { 

    public static volatile SingularAttribute<Invoice, Account> owner;
    public static volatile SingularAttribute<Invoice, String> urlToDownload;
    public static volatile SingularAttribute<Invoice, Double> totalAmount;
    public static volatile SingularAttribute<Invoice, String> licensePlate;
    public static volatile SingularAttribute<Invoice, Integer> month;
    public static volatile SingularAttribute<Invoice, Integer> year;
    public static volatile SingularAttribute<Invoice, Integer> kilometers;
    public static volatile SingularAttribute<Invoice, Boolean> paid;
    public static volatile SingularAttribute<Invoice, Long> id;

}