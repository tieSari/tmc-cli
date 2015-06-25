// älä koske tähän tiedostoon

import java.awt.HeadlessException;
import javax.swing.JFrame;

public class HirsipuuIkkuna extends JFrame {

    private HirsipuuPiirtoalusta alusta;

    public HirsipuuIkkuna(HirsipuuLogiikka logiikka) throws HeadlessException {
        super();
        setTitle("Hirsipuu");
        
        this.alusta = new HirsipuuPiirtoalusta(logiikka, this);
        add(this.alusta);
        addKeyListener(new HirsipuuKeyAdapter(logiikka));

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 400);
    }

    @Override
    public void repaint() {
        super.repaint();
        this.alusta.repaint();
    }

    public void pelaa() {
        setVisible(true);
    }
}
