package recipes.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import recipes.persistence.UserRepository;
import recipes.user.User;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepo;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepo.findUserByUsername(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("Not found: " + username);
        }
        return new UserDetailsImpl(user.get());
    }
}
