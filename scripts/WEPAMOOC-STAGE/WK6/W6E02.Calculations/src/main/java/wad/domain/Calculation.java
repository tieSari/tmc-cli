package wad.domain;

import javax.persistence.Entity;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
public class Calculation extends AbstractPersistable<Long> {

    private String content;
    private String status;
    private String calculationResult;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCalculationResult() {
        return calculationResult;
    }

    public void setCalculationResult(String calculationResult) {
        this.calculationResult = calculationResult;
    }

}
