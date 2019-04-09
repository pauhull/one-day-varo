package de.pauhull.onedayvaro.util;

import net.md_5.bungee.api.ChatColor;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class ChatFace {

    private static final String API_URL = "https://minotar.net/avatar/%s/8.png";

    public static String[] getLines(String identifier) {

        Color[] colors;

        try {
            colors = getFace(identifier);
        } catch (IOException e) {
            return new String[0];
        }

        ChatColor[] chatColors = new ChatColor[colors.length];

        for (int i = 0; i < colors.length; i++) {
            chatColors[i] = chatColorFromJavaColor(colors[i]);
        }

        String[] lines = new String[8];
        for (int i = 0; i < 8; i++) {
            StringBuilder builder = new StringBuilder();

            for (int j = 0; j < 8; j++) {
                builder.append(chatColors[j + i * 8]).append('█');
            }

            lines[i] = builder.toString();
        }

        return lines;
    }

    public static Color[] getFace(String identifier) throws IOException {

        BufferedImage image = ImageIO.read(new URL(String.format(API_URL, identifier)));
        Color[] colors = new Color[8 * 8];

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                colors[x + y * 8] = new Color(image.getRGB(x, y));
            }
        }

        return colors;
    }

    public static ChatColor chatColorFromJavaColor(Color javaColor) {

        char[] chars = "0123456789abcdef".toCharArray();
        ChatColor[] allowedChatColors = new ChatColor[chars.length];

        for (int i = 0; i < allowedChatColors.length; i++) {
            allowedChatColors[i] = ChatColor.getByChar(chars[i]);
        }

        ChatColor nearestColor = ChatColor.BLACK;
        double nearestDistance = Double.POSITIVE_INFINITY;

        for (ChatColor chatColor : allowedChatColors) {

            Color a = javaColorFromChatColor(chatColor);
            double distance = distance(a, javaColor);

            if (distance < nearestDistance) {
                nearestColor = chatColor;
                nearestDistance = distance;
            }
        }

        return nearestColor;
    }

    public static Color javaColorFromChatColor(ChatColor chatColor) {

        switch (chatColor) {
            case BLACK:
                return new Color(0);
            case DARK_BLUE:
                return new Color(0x0000AA);
            case DARK_GREEN:
                return new Color(0x00AA00);
            case DARK_AQUA:
                return new Color(0x00AAAA);
            case DARK_RED:
                return new Color(0xAA0000);
            case DARK_PURPLE:
                return new Color(0xAA00AA);
            case GOLD:
                return new Color(0xFFAA00);
            case GRAY:
                return new Color(0xAAAAAA);
            case DARK_GRAY:
                return new Color(0x555555);
            case BLUE:
                return new Color(0x5555FF);
            case GREEN:
                return new Color(0x55FF55);
            case AQUA:
                return new Color(0x55FFFF);
            case RED:
                return new Color(0xFF5555);
            case LIGHT_PURPLE:
                return new Color(0xFF55FF);
            case YELLOW:
                return new Color(0xFFFF55);
            case WHITE:
                return new Color(0xFFFFFF);
            default:
                throw new IllegalStateException("Unexpected value: " + chatColor);
        }
    }

    private static double distance(Color a, Color b) {
        return (b.getRed() - a.getRed()) * (b.getRed() - a.getRed())
                + (b.getGreen() - a.getGreen()) * (b.getGreen() - a.getGreen())
                + (b.getBlue() - a.getBlue()) * (b.getBlue() - a.getBlue());
    }

}
