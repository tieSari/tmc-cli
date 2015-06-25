package wad.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name = "PERSISTENT_LOGINS", indexes = {
    @Index(columnList = "series")})
public class CustomPersistentToken extends AbstractPersistable<Long> {

    @Column(name = "USERNAME")
    private String username;
    @Column(name = "TOKEN_VALUE")
    private String tokenValue;
    @Column(name = "SERIES")
    private String series;
    @Column(name = "LAST_USED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUsed;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTokenValue() {
        return tokenValue;
    }

    public void setTokenValue(String tokenValue) {
        this.tokenValue = tokenValue;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public Date getLastUsed() {
        return lastUsed;
    }

    public void setLastUsed(Date lastUsed) {
        this.lastUsed = lastUsed;
    }
}
