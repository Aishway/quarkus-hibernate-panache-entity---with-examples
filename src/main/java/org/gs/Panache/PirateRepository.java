package org.gs.Panache;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class PirateRepository implements PanacheRepository<Pirate> {

    public Pirate findByName(String name){
        return find("name", name).firstResult();
    }

    public List<Pirate> findAlive(){
        return list("status", "alive");
    }

    public List<Pirate> findDead(){
        return list("status", "dead");
    }

        }
