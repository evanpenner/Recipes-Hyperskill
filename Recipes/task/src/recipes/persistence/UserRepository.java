package recipes.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import recipes.user.User;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, String> {
    @Override
    Iterable<User> findAll();

    Optional<User> findUserByUsername(String username);
}
