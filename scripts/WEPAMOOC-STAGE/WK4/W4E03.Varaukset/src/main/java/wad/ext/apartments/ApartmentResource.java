package wad.ext.apartments;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

public class ApartmentResource extends Resource<Apartment> {

    public ApartmentResource() {
        super(new Apartment());
    }

    public ApartmentResource(Apartment content, Link... links) {
        super(content, links);
    }
}
