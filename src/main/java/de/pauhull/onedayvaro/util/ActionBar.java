package de.pauhull.onedayvaro.util;

// Project: onedayvaro
// Class created on 17.04.2019 by Paul
// Package de.pauhull.onedayvaro.util

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class ActionBar {

    private IChatBaseComponent component;

    public ActionBar(String text) {

        this.component = ChatSerializer.a("{\"text\": \"" + text + "\"}");
    }

    public void send(Player... players) {

        PacketPlayOutChat packet = new PacketPlayOutChat(component, (byte) 2);

        for (Player player : players) {
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        }
    }

}
