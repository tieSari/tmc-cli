package wad.service;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wad.domain.Calculation;
import wad.repository.CalculationRepository;

@Service
public class CalculationService {

    @Autowired
    private CalculationRepository calculationRepository;

public Calculation process(Calculation calc) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(CalculationService.class.getName()).log(Level.SEVERE, null, ex);
        }

        calc.setStatus("PROCESSED");
        calc.setCalculationResult(calc.getContent() + ";" + UUID.randomUUID().toString());
return calculationRepository.save(calc);
    }
}
