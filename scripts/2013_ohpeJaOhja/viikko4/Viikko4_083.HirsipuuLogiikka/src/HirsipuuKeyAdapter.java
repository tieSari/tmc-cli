// älä koske tähän tiedostoon

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class HirsipuuKeyAdapter extends KeyAdapter {

    private HirsipuuLogiikka logiikka;

    public HirsipuuKeyAdapter(HirsipuuLogiikka logiikka) {
        this.logiikka = logiikka;
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        if (this.logiikka.virheidenLukumaara() > this.logiikka.virheitaHavioon()) {
            return;
        }

        String salattuSana = this.logiikka.salattuSana();
        if (!salattuSana.contains("_")) {
            return;
        }

        super.keyPressed(ke);
        String key = "" + ke.getKeyChar();
        key = key.toUpperCase();
        this.logiikka.arvaaKirjain(key);
    }
}
