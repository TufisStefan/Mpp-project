package repository;

import domain.User;

public interface UserRepository extends CrudRepository<Long, User>{

    User login(String username, String password) throws RepositoryException;
}
