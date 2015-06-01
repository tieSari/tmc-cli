package hy.tmc.cli.frontend;


public class ColorFormatter {

    private static final String clearFormatting = "\u001B[0m";
    
    public static String coloredString(String text, CommandLineColor color) {
        return color.getColorCode(true) + text + clearFormatting;
    }
    
    public static String coloredString(String text, 
            CommandLineColor foreground, CommandLineColor background) {
        return foreground.getColorCode(true) + 
               background.getColorCode(false) +
               text + clearFormatting;
    }
}
