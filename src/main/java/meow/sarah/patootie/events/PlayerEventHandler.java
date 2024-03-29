package meow.sarah.patootie.events;

import meow.sarah.patootie.Sarah;
import meow.sarah.patootie.util.Utils;
import net.labymod.addon.AddonLoader;
import net.labymod.addons.voicechat.VoiceChat;
import net.labymod.api.LabyModAddon;
import net.labymod.main.LabyMod;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static meow.sarah.patootie.util.Utils.SetConfig;
import static meow.sarah.patootie.util.Utils.sendMessage;

public class PlayerEventHandler {

    // UUID VoiceCHat 1.12
    private final UUID vcUuid12 = UUID.fromString("24c0644d-ad56-4609-876d-6e9da3cc9794");
    // UUID VoiceChat 1.8
    private final UUID vcUuid8 = UUID.fromString("43152d5b-ca80-4b29-8f48-39fd63e48dee");

    // im gonna kms ngl
    Sarah instance = Sarah.getInstance();
    LabyMod labymod = LabyMod.getInstance();
    Minecraft minecraft = Minecraft.getMinecraft();
    LabyModAddon addon = AddonLoader.getAddonByUUID(UUID.fromString(String.valueOf(vcUuid8)));
    VoiceChat voiceChat = (VoiceChat) AddonLoader.getAddonByUUID(this.vcUuid8);

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (!instance.isVoiceexist()) {
            instance.setVoiceexist(addon instanceof VoiceChat && addon.about.name.equals("VoiceChat"));
        }
    }

    @SubscribeEvent
    public void onPrePlayerRender(RenderPlayerEvent.Pre e) {
        EntityPlayer enPlayer = e.entityPlayer;
        if (instance.isRenderPlayers() && instance.isModOn()) {
            if (instance.isRenderPlayers() && !enPlayer.equals(minecraft.thePlayer)) {
                List<String> localPlayersToRender = instance.getPlayersToRenderString();
                if (Utils.isNPC(enPlayer)) {
                    e.setCanceled(false);
                    for (String s : localPlayersToRender) {
                        if (s.equals(enPlayer.getGameProfile().getName())) {
                            e.setCanceled(true);
                        }
                    }
                }
            }
        }
    }

    public void mute(EntityPlayer player) {
        if (!player.equals(minecraft.thePlayer)) {
            voiceChat.getPlayerVolumes().put(player.getUniqueID(), 0);
            voiceChat.savePlayersVolumes();
        }
    }

    public void RemovePlayer(String s) {
        // remove from the list
        instance.getPlayersToRenderString().remove(s);
        instance.savePlayersToRenderString();
        //  playersToRenderString.removeIf(player -> player.equals(s));
        instance.saveConfig();
    }

    public void unmute(EntityPlayer player) {
        UUID uuid = player.getUniqueID();
        Map<UUID, Integer> volume = voiceChat.getPlayerVolumes();
        if (!player.equals(minecraft.thePlayer)) {
            volume.put(uuid, 100);
        }
        voiceChat.savePlayersVolumes();
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent e) {
        EntityPlayer enPlayer = minecraft.thePlayer;
        if (Keyboard.getEventKey() == instance.getKey() && !Keyboard.getEventKeyState() &&
                instance.isModOn() && instance.getKey() > -1) {
            if (instance.isRenderPlayers()) {
                SetConfig(false);
                if (instance.isVoiceexist() && instance.isPlayerUnmute()) {
                    minecraft.theWorld.playerEntities.stream()
                            .filter(entityPlayer ->
                                    instance.getPlayersToRenderString()
                                            .contains(entityPlayer.getName())).
                            forEach(this::unmute);
                }
                if (instance.isConfigMessage()) {
                    sendMessage("[PH] - Off");
                }
            } else {
                SetConfig(true);
                if (instance.isVoiceexist() && instance.isPlayerUnmute()) {
                    minecraft.theWorld.playerEntities.stream()
                            .filter(entityPlayer ->
                                    instance.getPlayersToRenderString()
                                            .contains(entityPlayer.getName())).
                            forEach(this::mute);
                }
                if (instance.isConfigMessage()) {
                    sendMessage("[PH] - On");
                }
            }
        } else if (Keyboard.getEventKey() == instance.getKey() &&
                !Keyboard.getEventKeyState() && !instance.isModOn() && instance.getKey() > -1) {
            sendMessage("[PH] - Mod seems to be disabled. Check Config.");
        }
    }
}