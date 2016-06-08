/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import service.IRekeningrijderService;

/**
 *
 * @author Gijs
 */
@Path("/status")
public class MonitoringEndpointREST {
    
    @EJB
    private IRekeningrijderService service;
    
    @GET
    @Path("/general")
    public Response getGeneralStatus() {
        return Response.ok().build();
    }
    
    @GET
    @Path("/database")
    public Response getDatabaseStatus() {
        if (service.DatabaseIsOnline()) {
            return Response.ok().build();
        }
        return Response.serverError().build();
    }
}
