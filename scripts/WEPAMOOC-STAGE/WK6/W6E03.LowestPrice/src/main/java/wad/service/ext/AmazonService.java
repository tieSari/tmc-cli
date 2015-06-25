package wad.service.ext;

import org.springframework.stereotype.Service;

@Service
public class AmazonService extends BaseService {

    @Override
    public String getName() {
        return "Amazon";
    }
}
