package me.third.right.utils.Client.Utils;

public class Colour {

    //Colour Converters
    public static float toF(int i) { return i / 255f; }

    public static float toF(double d) { return (float) (d / 255f); }

    public static int rgbToInt(int r, int g, int b, int a) { return (r << 16) | (g << 8) | (b) | (a << 24); }

    public static int rgbToInt(int r, int g, int b) { return (r << 16) | (g << 8) | (b); }

    //Multi purpose colour enum.
    public enum colour{
        BLACK("&0","§0",new int[] {0,0,0}),
        RED("&c","§c",new int[] {255,85,85}),
        AQUA("&b","§b",new int[] {85,255,255}),
        BLUE("&9","§9",new int[] {85,85,255}),
        GOLD ("&6","§6",new int[] {255,170,0}),
        GRAY("&7","§7",new int[] {170,170,170}),
        WHITE("&f","§f",new int[] {255,255,255}),
        GREEN("&a","§a",new int[] {85,255,85}),
        YELLOW("&e","§e",new int[] {255,255,85}),
        DARK_RED("&4","§4",new int[] {170,0,0}),
        DARK_AQUA("&3","§3",new int[] {0,170,170}),
        DARK_BLUE("&1","§1",new int[] {0,0,170}),
        DARK_GRAY("&8","§8",new int[] {85,85,85}),
        DARK_GREEN("&2","§2",new int[] {0,170,0}),
        DARK_PURPLE("&5","§5",new int[] {170,0,170}),
        LIGHT_PURPLE("&d","§d",new int[] {255,85,255});
        public final String codeChat;
        public final String codeUI;
        public final int[] codeToRgb;
        colour(String codeChat, String codeUI, int[] codeToRgb){
            this.codeChat = codeChat;
            this.codeUI = codeUI;
            this.codeToRgb = codeToRgb;
        }
    }

    public static int[] getColourRGB(colour colour){
        return colour.codeToRgb;
    }

    public static String getColourChat(colour colour){
        return colour.codeChat;
    }

    public static String getColourUI(colour colour){ return colour.codeUI; }
}
