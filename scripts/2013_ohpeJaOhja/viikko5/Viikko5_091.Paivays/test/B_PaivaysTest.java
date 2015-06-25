
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

//@RunWith(PowerMockRunner.class)
@PrepareForTest({Paivays.class})
public class B_PaivaysTest {

    @Points("91.2")
    @Test
    public void parametrillinenMetodiEteneKutsuuParametritontaEteneMetodia() {
        Class c = Paivays.class;
        String metodi = "etene";
        String virhe = "Tee luokalle Paivays metodi public void etene(int paivia)";
        int montakoPaivaaEteenpain = 3;

        Method parametritonEtene = null;
        try {
            parametritonEtene = ReflectionUtils.requireMethod(c, metodi);
        } catch (Throwable t) {
            fail(virhe);
        }

        Paivays mockPaivays = PowerMock.createMock(Paivays.class, parametritonEtene);

        try {
            parametritonEtene.invoke(mockPaivays);
        } catch (Throwable t) {
            fail(virhe + ", varmista myös että metodi on public.");
        }
        PowerMock.expectLastCall().times(montakoPaivaaEteenpain);

        PowerMock.replay(mockPaivays);

        Method parametrillinenEtene = null;
        try {
            parametrillinenEtene = ReflectionUtils.requireMethod(mockPaivays.getClass(), metodi, int.class);
        } catch (Throwable t) {
            fail("Varmista että kutsut metodia etene() metodista etene(int paivia). Tarkista myös että kutsukertojen määrä on oikea.");
        }
        try {
            parametrillinenEtene.invoke(mockPaivays, montakoPaivaaEteenpain);
        } catch (Throwable t) {
            fail("Varmista että kutsut metodia etene() metodista etene(int paivia). Tarkista myös että kutsukertojen määrä on oikea.");
        }

        try {
            PowerMock.verify(mockPaivays);
        } catch (Throwable t) {
            fail("Varmista että kutsut metodia etene() metodista etene(int paivia). Tarkista myös että kutsukertojen määrä on oikea." + t);
        }
    }
}
