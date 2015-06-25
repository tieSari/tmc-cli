package wad.domain;

import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
public class Twiit extends AbstractPersistable<Long> {

    @ManyToOne
    private Twiiter twiiter;

    @Length(max = 168) // 168 > 160
    private String content;

    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @OneToOne(cascade = CascadeType.ALL)
    private TwiitPic twiitPic;

    public Twiit() {
        this.created = new Date();
    }

    public Twiiter getTwiiter() {
        return twiiter;
    }

    public void setTwiiter(Twiiter twiiter) {
        this.twiiter = twiiter;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public TwiitPic getTwiitPic() {
        return twiitPic;
    }

    public void setTwiitPic(TwiitPic twiitPic) {
        this.twiitPic = twiitPic;
    }
}
