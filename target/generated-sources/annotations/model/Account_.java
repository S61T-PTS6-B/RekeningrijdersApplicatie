package model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-04-19T10:01:30")
@StaticMetamodel(Account.class)
public class Account_ { 

    public static volatile SingularAttribute<Account, String> password;
    public static volatile SingularAttribute<Account, Integer> bsn;
    public static volatile SingularAttribute<Account, String> confirmationId;
    public static volatile SingularAttribute<Account, Boolean> confirmed;
    public static volatile SingularAttribute<Account, String> email;

}