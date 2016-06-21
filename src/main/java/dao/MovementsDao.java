/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import model.Movement;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import utilities.AESEncrypt;

/**
 *
 * @author Gijs
 */
@Stateless
public class MovementsDao implements IMovementsDao {

    @Override
    public List<Movement> GetMovements(String licensePlate, int month, int year) throws Exception {
        List<Movement> movements = new ArrayList<>();
        Client client = ClientBuilder.newClient();
        String send = "id=" + licensePlate  + "&month=" + month + "&year=" + year;
        String encrp = AESEncrypt.encrypt(send);
        try {
            WebTarget resource = client.target("http://145.93.81.86:8080/VerplaatsingSysteem/Rest/carTrackers/getMonth?code=" + encrp);
            String response = resource.request(MediaType.APPLICATION_JSON).get(String.class);
            JSONObject obj = new JSONObject(AESEncrypt.decrypt(response));
            JSONArray arr = obj.getJSONArray("locations");
            for (int i=0; i<arr.length(); i++) {
                JSONObject beginobj = arr.getJSONObject(i);
                JSONObject endobj;
                try {
                    endobj = arr.getJSONObject(i+1);
                } catch (JSONException ex) {
                    endobj = beginobj;
                }
                Movement m = new Movement();
                m.setLatStart(beginobj.getDouble("lat"));
                m.setLongStart(beginobj.getDouble("long"));
                m.setLatEnd(endobj.getDouble("lat"));
                m.setLongEnd(endobj.getDouble("long"));
                DateFormat format = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy");         
                m.setDate(format.parse(beginobj.getString("date")));
                movements.add(m);
            } 
        } catch (Exception ex) {
            return null;
        }
        return movements;
    }
    
}
