package org.gs.Panache;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
@NamedQuery(name = "Ship.getByName" , query = "from Ship where name = ?1")

@Entity
public class Ship extends PanacheEntity {

    @Column
    public String name;

    @Column
    public String direction;

    @Column
    public  String size;

    @OneToMany(targetEntity = Pirate.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    public List<Pirate> listOfPirates = new ArrayList<>();

    public static Ship findByName(String name){
        return find("#Ship.getByName",name).firstResult();
    }

}
