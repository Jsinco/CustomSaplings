package me.jsinco.customsaplings.util;

import me.jsinco.customsaplings.CustomSaplings;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class TextUtils {
    private static final CustomSaplings plugin = CustomSaplings.getPlugin(CustomSaplings.class);
    public static final String WITH_DELIMITER = "((?<=%1$s)|(?=%1$s))";

    public static String prefix = colorcode(plugin.getConfig().getString("prefix"));;

    public static void reloadTextUtils() {
        prefix = colorcode(plugin.getConfig().getString("prefix"));
    }

    /**
     * @param text The string of text to apply color/effects to
     * @return Returns a string of text with color/effects applied
     */
    public static String colorcode(String text) {

        String[] texts = text.split(String.format(WITH_DELIMITER, "&"));

        StringBuilder finalText = new StringBuilder();

        for (int i = 0; i < texts.length; i++) {
            if (texts[i].equalsIgnoreCase("&")) {
                //get the next string
                i++;
                if (texts[i].charAt(0) == '#') {
                    finalText.append(net.md_5.bungee.api.ChatColor.of(texts[i].substring(0, 7)) + texts[i].substring(7));
                } else {
                    finalText.append(ChatColor.translateAlternateColorCodes('&', "&" + texts[i]));
                }
            } else {
                finalText.append(texts[i]);
            }
        }

        return finalText.toString();
    }

    /**
     * Applies colorcode to a list of strings
     * @param list The list of strings to apply colorcode to
     * @return Returns a list of strings with colorcode applied
     */
    public static List<String> colorArrayList(List<String> list) {
        List<String> coloredList = new ArrayList<>();
        for (String string : list) {
            coloredList.add(colorcode(string));
        }
        return coloredList;
    }

    /**
     * Applies colorcode to an array of strings
     * @param array The array of strings to apply colorcode to
     * @return Returns an array of strings with colorcode applied
     */
    public static String[] colorArray(String[] array) {
        for (int i = 0; i < array.length; i++) {
            array[i] = colorcode(array[i]);
        }
        return array;
    }
}
