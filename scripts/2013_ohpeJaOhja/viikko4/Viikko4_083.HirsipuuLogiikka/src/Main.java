public class Main {

    public static void main(String[] args) {
        HirsipuuLogiikka logiikka = new HirsipuuLogiikka("parametri");
        HirsipuuIkkuna peliIkkuna = new HirsipuuIkkuna(logiikka);
        peliIkkuna.pelaa();
    }
}
