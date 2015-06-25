// älä koske tähän tiedostoon

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import javax.swing.JPanel;

public class HirsipuuPiirtoalusta extends JPanel {

    private HirsipuuLogiikka logiikka;
    private HirsipuuIkkuna ikkuna;

    HirsipuuPiirtoalusta(HirsipuuLogiikka logiikka, HirsipuuIkkuna ikkuna) {
        super();
        setBackground(Color.WHITE);
        this.logiikka = logiikka;
        this.ikkuna = ikkuna;

    }

    @Override
    public void paint(Graphics grphcs) {
        super.paint(grphcs);
        paintHangman(grphcs);

        this.ikkuna.repaint();
    }

    private void paintHangman(Graphics g) {
        int virheita = this.logiikka.virheidenLukumaara();

        // piirretaan ukko
        int baseY = 200;

        if (virheita > 0) {    // runko
            g.drawLine(90, baseY, 200, baseY);
        }
        if (virheita > 1) {    // palkki
            g.drawLine(125, baseY, 125, baseY - 100);
        }
        if (virheita > 2) {
            g.drawLine(110, baseY, 125, baseY - 15);
        }
        if (virheita > 3) {
            g.drawLine(140, baseY, 125, baseY - 15);
        }
        if (virheita > 4) {    // tuki
            g.drawLine(125, baseY - 100, 175, baseY - 100);
        }
        if (virheita > 5) {
            g.drawLine(125, baseY - 85, 140, baseY - 100);
        }
        if (virheita > 6) {    // naru
            g.drawLine(175, baseY - 100, 175, baseY - 75);
        }
        if (virheita > 7) {    // ruumis
            g.drawOval(170, baseY - 75, 10, 12);
        }
        if (virheita > 8) {
            g.drawOval(170, baseY - 65, 15, 25);
        }
        if (virheita > 9) {    // kadet
            g.drawLine(160, baseY - 65, 170, baseY - 60);
        }
        if (virheita > 10) {
            g.drawLine(183, baseY - 60, 193, baseY - 65);
        }
        if (virheita > 11) {    // jalat
            g.drawLine(165, baseY - 30, 170, baseY - 45);
        }
        if (virheita > 12) {
            g.drawLine(183, baseY - 45, 193, baseY - 30);
        }

        // näytä sana
        g.drawString("Sana: " + this.logiikka.salattuSana(), 20, 250);

        // näytä virheiden lkm
        g.drawString("Virheitä: " + virheita, 20, 270);


        // näytä viesti
        g.drawString("Viesti: " + annaTilanne(), 20, 290);

        // näytä arvatut kirjaimet
        drawString(g, "Arvatut: " + this.logiikka.arvatutKirjaimet(), 20, 310, 240);
    }

    public String annaTilanne() {
        String salattuSana = this.logiikka.salattuSana();
        if (!salattuSana.contains("_")) {
            return "Voitit!";
        }

        if (logiikka.virheidenLukumaara() > this.logiikka.virheitaHavioon()) {
            return "Hävisit!";
        }

        return "Syötä kirjain!";
    }

    // piirretään tekstiä joka vaihtaa riviä tarvittaessa
    public void drawString(Graphics g, String string, int x, int y, int width) {
        FontMetrics fontMetrics = g.getFontMetrics();
        int lineHeight = fontMetrics.getHeight();

        int curX = x;
        int curY = y;

        String[] words = string.split(" ");

        for (String word : words) {
            // Find out thw width of the word.
            int wordWidth = fontMetrics.stringWidth(word + " ");

            // If text exceeds the width, then move to next line.
            if (curX + wordWidth >= x + width) {
                curY += lineHeight;
                curX = x;
            }

            g.drawString(word, curX, curY);

            // Move over to the right for next word.
            curX += wordWidth;
        }
    }
}
