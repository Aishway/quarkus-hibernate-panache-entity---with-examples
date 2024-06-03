package org.gs.Panache;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.util.List;

@Path("/ship")
public class PirateShipREST_API {

    @Inject
    EntityManager em;

    @Inject
    PirateRepository pirateRepository;

    @GET
    @Path("/test")
    @Produces(MediaType.APPLICATION_JSON)
    public Response test (){
        return  Response.ok("Say Hello").build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getShips() {
        List<Ship> ships = Ship.listAll();
        return Response.ok(ships).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByID(@PathParam("id") Long id) {
        return Ship.findByIdOptional(id)
                .map(ship -> Response.ok(ship).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @GET
    @Path("/direction/{direction}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByDirection(@PathParam("direction") String direction){
       Query q = em.createNativeQuery("SELECT * FROM Ship WHERE direction = ?1");
       q.setParameter(1, direction);
       List <Object[]> ships = q.getResultList();
        return Response.ok(ships).build();
    }

    @POST
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create (Ship ship) {
        Ship.persist(ship);
        if(ship.isPersistent()){
            return Response.created(URI.create("/ship" + ship.id)).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @DELETE
    @Transactional
    @Path("{id}")
    public Response deleteByID(@PathParam("id")int id){
        boolean deleted = Ship.deleteById(id);
        return deleted ? Response.noContent().build() : Response.status(Response.Status.BAD_REQUEST).build();
    }

    @PUT
    @Path("addPirates/{shipID}/{pirateID}")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPirateToShip (@PathParam("shipID") int shipID, @PathParam("pirateID") Long pirateID){
        Ship ship = Ship.findById(shipID);
        if(ship== null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Pirate pirate = pirateRepository.findById(pirateID);
        ship.listOfPirates.add(pirate);
        return Response.ok(Ship.findByIdOptional(shipID)).build();
    }


    /********************        EXAMPLES     *******************************/

    @POST
    @Path("addShipList")
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPirates (List<Ship> list){
        if(list.isEmpty()){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        list.stream().forEach(ship -> Ship.persist(ship));
        return Response.ok(Ship.listAll() ).build();
    }

    @GET
    @Path("listLargeShips")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLargeShips (){
       // Ship.findAll().filter( "size = large");
        return Response.ok(Ship.list("size", "large")).build();
    }

    @PUT
    @Path("changeName/{oldName}/{newName}")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Response changeName (@PathParam("oldName") String oldName, @PathParam("newName") String newName){
        Ship ship = Ship.findByName(oldName);
        if(ship== null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        ship.name = newName;
        return Response.ok(Ship.findByIdOptional(ship.id)).build();
    }

}
