package kassuk.addon.blackout.modules.chat;

import kassuk.addon.blackout.BlackOut;
import kassuk.addon.blackout.utils.Wrapper;
import kassuk.addon.blackout.utils.random.AutomationUtils;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;
import java.util.List;

public class BurrowAlert extends Module {

    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final Setting<Integer> range = sgGeneral.add(new IntSetting.Builder().name("range").description("How far away from you to check for burrowed players.").defaultValue(2).min(0).sliderMax(10).build());

    public BurrowAlert() {
        super(BlackOut.CHATPLUS, "burrow-alert", "Alerts you when players are burrowed.");
    }

    private int burrowMsgWait;
    public static List<PlayerEntity> burrowedPlayers = new ArrayList<>();

    @Override
    public void onActivate() {
        burrowMsgWait = 0;

    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        for (PlayerEntity player : mc.world.getPlayers()) {
            if (isValid(player)) {
                burrowedPlayers.add(player);
                warning("[League]" + player.getName() + " is burrowed!");
            }
            if (burrowedPlayers.contains(player) && !AutomationUtils.isBurrowed(player, true)) {
                burrowedPlayers.remove(player);
                warning("[League]]" + player.getName() + " is no longer burrowed.");
            }
        }
    }

    private boolean isValid(PlayerEntity p) {
        if (p == mc.player) return false;
        return mc.player.distanceTo(p) <= range.get() && !burrowedPlayers.contains(p) && AutomationUtils.isBurrowed(p, true) && !Wrapper.isPlayerMoving(p);
    }



}
