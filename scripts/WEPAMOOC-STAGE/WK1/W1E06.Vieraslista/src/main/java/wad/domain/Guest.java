package wad.domain;

import java.util.UUID;

public class Guest {

    private String id = UUID.randomUUID().toString().substring(0, 6);
    private String name;
    private String menu;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

}
