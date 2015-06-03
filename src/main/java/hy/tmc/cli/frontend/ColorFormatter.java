package hy.tmc.cli.frontend;


public class ColorFormatter {

    private static final String clearFormatting = "\u001B[0m";
    
    /**
     * Add color formatting codes to text. 
     * @color the foreground color to be used
     * @return a string that will be colored with the specified color
     */
    public static String coloredString(String text, CommandLineColor color) {
        return color.getColorCode(true) + text + clearFormatting;
    }
    
    /**
     * Add color formatting codes to text.
     */
    public static String coloredString(String text, 
            CommandLineColor foreground, CommandLineColor background) {
        return foreground.getColorCode(true) 
               + background.getColorCode(false)
               + text + clearFormatting;
    }
}
