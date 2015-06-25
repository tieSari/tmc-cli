package wad.service.ext;

import org.springframework.stereotype.Service;

@Service
public class EBayService extends BaseService {

    @Override
    public String getName() {
        return "EBay";
    }

}
