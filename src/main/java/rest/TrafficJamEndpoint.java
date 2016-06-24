/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import javax.ejb.EJB;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import service.IRekeningrijderService;

/**
 *
 * @author Gijs
 */
@Path("/trafficjam")
public class TrafficJamEndpoint {
    
    @EJB
    private IRekeningrijderService service;
    
    @POST
    @Path("/{roadname}")
    public Response informTrafficJam(@PathParam("roadname") String road) {
        return null;
    }
}