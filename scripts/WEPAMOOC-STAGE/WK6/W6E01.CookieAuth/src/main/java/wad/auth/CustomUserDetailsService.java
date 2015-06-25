package wad.auth;

import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import wad.domain.AppUser;
import wad.repository.AppUserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private AppUserRepository userRepository;

    @PostConstruct
    public void init() {
        if (userRepository.count() > 1) {
            userRepository.deleteAll();
        }

        AppUser user = new AppUser();
        user.setPassword("password");
        user.setUsername("username");

        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<AppUser> users = userRepository.findByUsername(username);
        if (users == null || users.isEmpty()) {
            throw new UsernameNotFoundException("No such user " + username);
        }

        return new User(users.get(0).getUsername(), users.get(0).getPassword(), Arrays.asList(new SimpleGrantedAuthority("USER")));
    }
}
