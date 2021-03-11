package repository;

import domain.User;

public interface UserRepository extends CrudRepository<Long, User>{

    void login(String username, String password);
}
