package wad.service.ext;

import org.springframework.stereotype.Service;

@Service
public class KatiskaService extends BaseService {

    @Override
    public String getName() {
        return "Verkkokauppa";
    }
}
