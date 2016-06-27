/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import utilities.AESEncrypt;

/**
 *
 * @author casva
 */
@Path("/encrypt")
public class EncryptRest {
        
        @GET
        @Produces("text/plain")
    @Path("/{roadname}")
    public Response RemoveTrafficJam(@PathParam("roadname") String road) throws Exception {
       System.out.print(road);
       
       return Response.status(200).entity(AESEncrypt.encrypt(road)).build();
    }
}
