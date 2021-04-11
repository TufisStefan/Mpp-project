package repository;

import domain.Entity;
import domain.validators.ValidationException;

import java.util.List;

public interface CrudRepository <ID, E extends Entity<ID>>{

    /**
     * @param id must not be null
     * @return the entity with given ID
     */
    E findOne(ID id);

    /**
     *
     * @return all entities
     */
    List<E> findAll();


    /**
     *
     * @param entity
     *         entity must be not null
     * @return null- if the given entity is saved
     *         otherwise returns the entity (id already exists)
     * @throws IllegalArgumentException
     *             if the given entity is null.
     */
    void save(E entity) throws ValidationException;


    /**
     *  removes the entity with the specified id
     * @param id
     *      id must be not null
     * @return the removed entity or null if there is no entity with the given id
     * @throws IllegalArgumentException
     *                   if the given id is null.
     */
    void delete(ID id);

    /**
     *
     * @param entity
     *          entity must not be null
     * @return null - if the entity is updated,
     *                otherwise  returns the entity.
     * @throws IllegalArgumentException
     *             if the given entity is null.
     */
    void update(ID id,E entity) throws ValidationException;
}
