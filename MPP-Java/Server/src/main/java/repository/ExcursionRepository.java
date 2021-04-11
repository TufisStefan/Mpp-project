package repository;

import domain.Excursion;
import domain.validators.ExcursionValidator;

import java.time.LocalTime;
import java.util.List;

public interface ExcursionRepository extends CrudRepository<Long, Excursion>{


    List<Excursion> filterByObjective(String objective, LocalTime from, LocalTime to);

}
