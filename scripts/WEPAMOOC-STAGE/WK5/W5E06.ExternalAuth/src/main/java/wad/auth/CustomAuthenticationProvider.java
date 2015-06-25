package wad.auth;

import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private static final String AUTH_URI = "http://authebin.herokuapp.com/authenticate";

    private final RestTemplate restTemplate;

    public CustomAuthenticationProvider() {
        this.restTemplate = new RestTemplate();
    }

    @Override
    public Authentication authenticate(Authentication a) throws AuthenticationException {

        List<GrantedAuthority> grantedAuths = new ArrayList<>();
        grantedAuths.add(new SimpleGrantedAuthority("USER"));

        return new UsernamePasswordAuthenticationToken("le username from le real service", "le password", grantedAuths);
    }

    @Override
    public boolean supports(Class<?> type) {
        return true;
    }

}
