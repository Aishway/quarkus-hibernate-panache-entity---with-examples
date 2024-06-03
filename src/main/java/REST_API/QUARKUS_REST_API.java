package REST_API;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.*;

@Path("test")
public class QUARKUS_REST_API {
    private List<Ship_R> shipList = new ArrayList<>();
    private List<Pirate_R> pirateList = new ArrayList<>();

    @GET
    public Response test(){
    return Response.ok("Always be yourself, unless you can be a pirate. Then always be a pirate.").build();
    }

    @POST
    @Path("addShip")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addShip(List<Ship_R> list){
        if (list.isEmpty()){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        this.shipList.addAll(list);
        return Response.ok(this.shipList ).build();
    }

    @POST
    @Path("addPirate")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addPirate(List<Pirate_R> list){
        if (list.isEmpty()){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        this.pirateList.addAll(list);
        return Response.ok(this.pirateList ).build();
    }

    @PUT
    @Path("editNickname/{old}/{new}")
    @Produces(MediaType.APPLICATION_JSON)
        public Response editNickname(@PathParam("old") String oldNickname, @PathParam("new") String newNickname){
        Optional<Pirate_R> pirate = this.pirateList.stream().filter(p -> p.getNickname().equals(oldNickname)).findFirst();
        if (pirate.isEmpty()){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        pirate.get().setNickname(newNickname);
        return Response.ok(pirate.get().toString() + "old Nickname: " + oldNickname +//
                 " changed to " + newNickname ).build();
    }

    @DELETE
    @Path("deletePirate/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deletePirate(@PathParam("name") String name){
        Optional<Pirate_R> pirate = this.pirateList.stream().filter(p -> p.getName().equals(name)).findFirst();
        if (pirate.isEmpty()){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        pirateList.remove(pirate.get());
        return Response.ok(pirate.get().toString() + " removed" ).build();
    }

    /*********                          EXAMPLES                              ******************/

    @GET
    @Path("numberOfPirates")
    @Produces(MediaType.APPLICATION_JSON)
    public Response numberOfPirates(){
        if(this.pirateList.isEmpty()){
            return Response.ok("List of pirates is empty").build();
        }
        return Response.ok("We have " + this.pirateList.size() + " pirates in list.").build();
    }

    @GET
    @Path("sort")
    @Produces(MediaType.APPLICATION_JSON)
    public Response sortByName(){
        this.pirateList.sort(new Comparator<Pirate_R>() {
            @Override
            public int compare(Pirate_R p1, Pirate_R p2) {
                String firstPirateName = p1.getName();
                String secondPirateName = p2.getName();
                return firstPirateName.compareTo(secondPirateName);
        }});

    return Response.ok(this.pirateList).build();
    }

    @POST
    @Path("create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create( Object[] array){

        if (array.length < 2){
        return Response.noContent().build();
    }

    LinkedHashMap shipValues = (LinkedHashMap) array[0];
    LinkedHashMap pirateValues = (LinkedHashMap) array[1];

    Ship_R ship = new Ship_R(shipValues.get("name").toString(), //
                                shipValues.get("direction").toString(),//
                                shipValues.get("size").toString() );

    Pirate_R pirate = new Pirate_R(pirateValues.get("name").toString(), //
            pirateValues.get("nickname").toString(),//
            pirateValues.get("function").toString(),
            pirateValues.get("status").toString());

        this.shipList.add(ship);
        this.pirateList.add(pirate);

        return Response.ok("Ship " + ship.toString()  +
                " pirate " + pirate + " was created.").build();
    }

    @PUT
    @Path("changeStatus/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response changeStatus(@PathParam("name") String name){
        Optional<Pirate_R> p= this.pirateList.stream().filter(pirate -> pirate.getName().equals(name)).findFirst();
        if(p.isEmpty()){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        p.get().setStatus("dead");
        return Response.ok(p).build();
    }

    @DELETE
    @Path("deleteShips/{character}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteShips(@PathParam("character") String character){
        List<Ship_R> shipsToDelete = this.shipList.stream().filter(ship -> ship.getName().startsWith(character)).toList();

        if(shipsToDelete.isEmpty()){
            return Response.status(Response.Status.NOT_FOUND).build();}

        this.shipList.removeAll(shipsToDelete);
        return Response.ok(this.shipList).build();
    }

}
