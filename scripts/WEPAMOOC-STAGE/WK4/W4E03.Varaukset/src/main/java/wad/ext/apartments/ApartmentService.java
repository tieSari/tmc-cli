package wad.ext.apartments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ApartmentService {

    @Autowired
    private RestTemplate restTemplate;

    private Map<Long, Apartment> apartments;

    private String uri;

    public final void setUri(String uri) {
        this.uri = uri;
    }

    // ability to set cache
    public void setApartments(Map<Long, Apartment> apartments) {
        this.apartments = apartments;
    }

    public List<Apartment> list() {
        if (this.apartments != null) {
            return new ArrayList<>(apartments.values());
        }

        ResponseEntity<Resources<Resource<Apartment>>> response
                = restTemplate.exchange(this.uri + "/apartments", // osoite
                        HttpMethod.GET, // metodi
                        null,
                        new ParameterizedTypeReference<Resources<Resource<Apartment>>>() {
                        }); // vastaustyyppi

        if (response.getStatusCode() == HttpStatus.OK) {
            Resources<Resource<Apartment>> resources = response.getBody();

            List<Apartment> data = new ArrayList<>();

            for (Resource<Apartment> resource : resources) {
                Apartment apa = resource.getContent();
                apa.setLink(resource.getId().getHref());

                if (apa.getId() == null) {
                    try {
                        apa.setId(Long.parseLong(apa.getLink().substring(apa.getLink().lastIndexOf("/") + 1)));
                    } catch (Throwable t) {
                    }
                }

                data.add(apa);
            }

            apartments = new HashMap<>();
            for (Apartment apartment : data) {
                apartments.put(apartment.getId(), apartment);
            }

            return data;
        }

        return Collections.EMPTY_LIST;
    }

    public Apartment get(Long key) {
        if(apartments == null) {
            list();
        }
        
        return apartments.get(key);
    }
}
