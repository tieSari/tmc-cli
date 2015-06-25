package wad.auth;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomPersistentTokenService implements PersistentTokenRepository {

    @Override
    public void createNewToken(PersistentRememberMeToken token) {
    }

    @Override
    public void updateToken(String series, String tokenValue, Date lastUsed) {
    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(String series) {
        return null;
    }

    @Override
    public void removeUserTokens(String username) {
    }
}
