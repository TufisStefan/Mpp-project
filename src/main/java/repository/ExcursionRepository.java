package repository;

import domain.Excursion;

import java.time.LocalTime;
import java.util.ArrayList;

public interface ExcursionRepository extends CrudRepository<Long, Excursion>{

    ArrayList<Excursion> filterByObjective(String objective, LocalTime from, LocalTime to);

}
