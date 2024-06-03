package org.gs.Panache;

import io.quarkus.panache.common.Sort;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/pirate")
public class PirateREST_API {

    @Inject
    PirateRepository pirateRepository;

    @Inject
    EntityManager em;

    @GET
    public Response countPirates(){
        return Response.ok(pirateRepository.listAll()).build();
    }

    @POST
    @Path("/addPirate")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create (Pirate pirate) {
         pirateRepository.persist(pirate);
        if(pirateRepository.isPersistent(pirate)){
            pirateRepository.flush();
            return Response.ok(pirate.toString()).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @PUT
    @Path("/status/{id}")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response changePirateStatus(@PathParam("id") Long id){
        Pirate pirate = pirateRepository.findById(id);
        pirate.setStatus("dead");
        pirateRepository.persistAndFlush(pirate);
        return Response.ok(pirateRepository.findDead()).build();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)

    public Response deleteByID(@PathParam("id") Long id){
        Pirate pirate = pirateRepository.findById(id);
        pirateRepository.delete(pirate);
        pirateRepository.flush();
        return Response.ok("successfully deleted").build();
    }


    /********************        EXAMPLES     *******************************/

    @POST
    @Path("addPirateList")
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPirates (List<Pirate> list){
        if(list.isEmpty()){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
                list.stream().forEach(pirate -> pirateRepository.persistAndFlush(pirate));
        return Response.ok(pirateRepository.listAll() ).build();
    }

    @GET
    @Path("alivePirates")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response returnListOfAlivePirates(){
        return Response.ok(pirateRepository.findAlive()).build();
    }

    @GET
    @Path("countAlive")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response returnNumberOfAlivePirates(){
        return Response.ok(pirateRepository.findAlive().size()).build();
    }

    @PUT
    @Path("changeName/{oldName}/{newName}")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Response changePirateName(@PathParam("oldName") String oldName, @PathParam("newName") String newName){
        Pirate pirate = pirateRepository.findByName(oldName);
        if (pirate == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        pirate.setName(newName);
        pirateRepository.flush();
        return Response.ok(pirateRepository.findByName(newName)).build();
    }

    @GET
    @Path("findCaptain")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findCaptain(){
        Query q = em.createNativeQuery("SELECT * FROM Pirate WHERE function = ?1");
        q.setParameter(1, "captain");
        Object pirate= q.getSingleResult();
        return Response.ok(pirate).build();
    }

    @GET
    @Path("sort")
    @Produces(MediaType.APPLICATION_JSON)
    public Response sortPirates(){
        List<Pirate> list = pirateRepository.list("order by name, function");
        return Response.ok(list).build();
    }








}
