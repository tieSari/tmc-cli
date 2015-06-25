package wad.service.ext;

import org.springframework.stereotype.Service;

@Service
public class HuutoNetService extends BaseService {

    @Override
    public String getName() {
        return "HuutoNet";
    }
}
