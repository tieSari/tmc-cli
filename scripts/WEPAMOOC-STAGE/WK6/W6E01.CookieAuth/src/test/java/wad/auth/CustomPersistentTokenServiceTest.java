package wad.auth;

import fi.helsinki.cs.tmc.edutestutils.Points;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;
import wad.Application;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@ActiveProfiles("test")
@Points("W6E01.1 W6E01.2")
public class CustomPersistentTokenServiceTest {

    @Autowired
    private WebApplicationContext webAppContext;

    @Autowired
    private CustomPersistentTokenService tokenService;

    @Autowired
    private JdbcTemplate template;

    @After
    public void cleanup() {
        template.execute("delete from PERSISTENT_LOGINS");

    }

    @Test
    public void testCreateNewTokenInsertsCorrectData() {
        Date currentDate = new Date();
        PersistentRememberMeToken token = new PersistentRememberMeToken("joeuser", "joesseries", "atoken", currentDate);
        tokenService.createNewToken(token);

        Map results = template.queryForMap("select * from PERSISTENT_LOGINS");

        assertEquals(currentDate, results.get("LAST_USED"));
        assertEquals("joeuser", results.get("USERNAME"));
        assertEquals("joesseries", results.get("SERIES"));
        assertEquals("atoken", results.get("TOKEN_VALUE"));
    }

    @Test
    public void retrievingTokenReturnsCorrectData() {

        template.execute("insert into PERSISTENT_LOGINS (SERIES, USERNAME, TOKEN_VALUE, LAST_USED) values "
                + "('joesseries', 'joeuser', 'atoken', '2007-10-09 18:19:25.000000000')");
        PersistentRememberMeToken token = tokenService.getTokenForSeries("joesseries");

        assertEquals("joeuser", token.getUsername());
        assertEquals("joesseries", token.getSeries());
        assertEquals("atoken", token.getTokenValue());
        assertEquals(Timestamp.valueOf("2007-10-09 18:19:25.000000000"), token.getDate());
    }

    @Test
    public void retrievingTokenWithDuplicateSeriesReturnsNull() {
        template.execute("insert into PERSISTENT_LOGINS (SERIES, USERNAME, TOKEN_VALUE, LAST_USED) values "
                + "('joesseries', 'joeuser', 'atoken2', '2007-10-19 18:19:25.000000000')");
        template.execute("insert into PERSISTENT_LOGINS (SERIES, USERNAME, TOKEN_VALUE, LAST_USED) values "
                + "('joesseries', 'joeuser', 'atoken', '2007-10-09 18:19:25.000000000')");

        assertNull(tokenService.getTokenForSeries("joesseries"));
    }

    @Test
    public void removingUserTokensDeletesData() {
        template.execute("insert into PERSISTENT_LOGINS (SERIES, USERNAME, TOKEN_VALUE, LAST_USED) values "
                + "('joesseries2', 'joeuser', 'atoken2', '2007-10-19 18:19:25.000000000')");
        template.execute("insert into PERSISTENT_LOGINS (SERIES, USERNAME, TOKEN_VALUE, LAST_USED) values "
                + "('joesseries', 'joeuser', 'atoken', '2007-10-09 18:19:25.000000000')");

        tokenService.removeUserTokens("joeuser");

        List results = template.queryForList("select * from PERSISTENT_LOGINS where USERNAME = 'joeuser'");

        assertEquals(0, results.size());
    }

    @Test
    public void updatingTokenModifiesTokenValueAndLastUsed() {
        Timestamp ts = new Timestamp(System.currentTimeMillis() - 1);
        template.execute("insert into PERSISTENT_LOGINS (SERIES, USERNAME, TOKEN_VALUE, LAST_USED) values "
                + "('joesseries', 'joeuser', 'atoken', '" + ts.toString() + "')");
        tokenService.updateToken("joesseries", "newtoken", new Date());

        Map results = template.queryForMap("select * from PERSISTENT_LOGINS where SERIES = 'joesseries'");

        assertEquals("joeuser", results.get("USERNAME"));
        assertEquals("joesseries", results.get("SERIES"));
        assertEquals("newtoken", results.get("TOKEN_VALUE"));
        Date lastUsed = (Date) results.get("LAST_USED");
        assertTrue(lastUsed.getTime() > ts.getTime());
    }
}
