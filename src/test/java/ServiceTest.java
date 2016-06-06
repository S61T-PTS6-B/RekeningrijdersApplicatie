/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import dao.IAccountDao;
import model.Account;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;
import service.IRekeningrijderService;
import service.RekeningrijderService;

/**
 *
 * @author Gijs
 */
@RunWith(MockitoJUnitRunner.class)
public class ServiceTest {
    
    private IRekeningrijderService service;
    @Mock
    private IAccountDao dao;
    private Account acc1, acc2;
    
    @Before
    public void setUp() {
        service = new RekeningrijderService();
        service.setDao(dao);
        acc1 = new Account();
        acc1.setBsn(218180901);
        acc1.setEmail("test1@hotmail.com");
        acc1.setPassword("test");
        acc1.setConfirmed(true);
        acc2 = new Account();
        acc2.setBsn(210494554);
        acc2.setEmail("test2@hotmail.com");
        acc2.setPassword("test");
        acc2.setConfirmed(true);
    }
    
    @Test
    public void LogInTest() {
        when(dao.AuthenticateUser(acc1.getBsn(), acc1.getPassword())).thenReturn(acc1);
        when(dao.UserIsActivated(acc1.getBsn())).thenReturn(true);
        assertEquals(acc1,service.LogIn(acc1.getBsn(), acc1.getPassword()));       
    }
    
    @Test
    public void ActivatedTest() {
        when(dao.UserIsActivated(acc1.getBsn())).thenReturn(true);
        assertFalse(service.ActivateUser(acc1.getBsn() + "", "jhh"));
    }
}
