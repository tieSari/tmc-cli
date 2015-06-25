package wad.domain;

import java.io.Serializable;
import java.util.Objects;

public class Pair implements Serializable {

    private String nameOne;
    private String nameTwo;

    public String getNameOne() {
        return nameOne;
    }

    public void setNameOne(String nameOne) {
        this.nameOne = nameOne;
    }

    public String getNameTwo() {
        return nameTwo;
    }

    public void setNameTwo(String nameTwo) {
        this.nameTwo = nameTwo;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.nameOne);
        hash = 59 * hash + Objects.hashCode(this.nameTwo);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Pair other = (Pair) obj;
        if (!Objects.equals(this.nameOne, other.nameOne)) {
            return false;
        }
        if (!Objects.equals(this.nameTwo, other.nameTwo)) {
            return false;
        }
        return true;
    }
    
    
}
