public class BinaariHaku {
    public static boolean hae(int[] taulukko, int etsittavaLuku) {
        int alku = 0;
        int loppu = taulukko.length - 1;

        while (alku <= loppu) {
            int puolivali = (alku + loppu) / 2;
            if (taulukko[puolivali] == etsittavaLuku) {
                return true;
            }

            // rajoita etsintäaluetta sopivasti
        }
        return false;
    }
}
