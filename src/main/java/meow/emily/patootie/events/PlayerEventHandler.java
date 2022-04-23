package meow.emily.patootie.events;

import com.mojang.realmsclient.gui.ChatFormatting;
import meow.emily.patootie.Emily;
import meow.emily.patootie.util.Utils;
import net.labymod.main.LabyMod;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class PlayerEventHandler {

    @SubscribeEvent
    public void onPrePlayerRender(RenderPlayerEvent.Pre e) {
        Emily instance = Emily.getInstance();
        if (instance.isRenderPlayers()) {
            EntityPlayer enPlayer = e.entityPlayer;
            if (instance.isRenderPlayers() && !enPlayer.equals(Minecraft.getMinecraft().thePlayer)) {
                List<String> localPlayersToRender = instance.getPlayersToRenderString();
                if (!Utils.isNPC(enPlayer)) {
                    e.setCanceled(false);
                    for (String s : localPlayersToRender) {
                        if (s.equals(enPlayer.getGameProfile().getName())) {
                            e.setCanceled(true);
                           /* if (instance.isVoiceexist()) {
                                if (instance.isMuted()) {
                                    try {
                                        mute(enPlayer);
                                        LabyMod.getInstance().displayMessageInChat("§c" + enPlayer.getGameProfile().getName() + " is muted");
                                    } catch (Exception ex) {
                                        LabyMod.getInstance().displayMessageInChat("§c" + ex);
                                    }
                                } else {
                                    try {
                                        unmute(enPlayer);
                                        LabyMod.getInstance().displayMessageInChat("§a" + enPlayer.getGameProfile().getName() + " is unmuted");
                                    } catch (Exception ex) {
                                        LabyMod.getInstance().displayMessageInChat("§c" + ex);
                                    }
                                }*/
                        }
                    }
                }
            }
        }
    }

    // Needs fixing. Crashes. idk why
    /*
    public void mute(EntityPlayer player) {
        Emily instance = Emily.getInstance();
        UUID uuid = player.getUniqueID();
        instance.getVoiceChat().getPlayerVolumes().put(uuid, 0);
        instance.saveConfig();
        instance.savePlayersToRender();
        instance.loadConfig();
    }

    public void unmute(EntityPlayer player) {
        Emily instance = Emily.getInstance();
        Map<UUID, Integer> playerVolumes = instance.getVoiceChat().getPlayerVolumes();
        UUID uuid = player.getUniqueID();
        if (playerVolumes.containsKey(uuid)) {
            playerVolumes.put(uuid, instance.getVoiceChat().getVolume(uuid));
        } else {
            playerVolumes.put(uuid, 100);
        }
        instance.saveConfig();
        instance.savePlayersToRender();
        instance.loadConfig();
    } */

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent e) {
        Emily instance = Emily.getInstance();
        if (instance.getKey() > -1) {
            if (Keyboard.isKeyDown(instance.getKey())) {
                LabyMod labymod = LabyMod.getInstance();
                if (instance.isRenderPlayers()) {
                    instance.setRenderPlayers(false);
                    instance.setMuted(false);
                    if (instance.isConfigMessage()) {
                        labymod.displayMessageInChat(ChatFormatting.GRAY + ">>" + "[" + ChatFormatting.AQUA + "PH" + ChatFormatting.WHITE + "]" + ChatFormatting.BOLD + ChatFormatting.GREEN + " on");
                    }
                } else {
                    instance.setRenderPlayers(true);
                    instance.setMuted(true);
                    if (instance.isConfigMessage()) {
                        labymod.displayMessageInChat(ChatFormatting.GRAY + ">>" + "[" + ChatFormatting.AQUA + "PH" + ChatFormatting.WHITE + "]" + ChatFormatting.BOLD + ChatFormatting.DARK_RED + " off");
                    }
                }
            }
        }
    }
}
