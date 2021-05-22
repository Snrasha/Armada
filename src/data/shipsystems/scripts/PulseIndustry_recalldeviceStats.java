package src.data.shipsystems.scripts;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;
import java.awt.Color;

public class PulseIndustry_recalldeviceStats extends BaseShipSystemScript {

    public static final Object KEY_JITTER = new Object();
    public static final Color JITTER_COLOR = new Color(100, 165, 255, 155);

    @Override
    public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {
        ShipAPI ship = null;
        if (stats.getEntity() instanceof ShipAPI) {
            ship = (ShipAPI) stats.getEntity();
        } else {
            return;
        }

        if (effectLevel > 0) {
            float jitterLevel = effectLevel;

            boolean firstTime = false;
            final String fightersKey = ship.getId() + "_recall_device";

            if (!Global.getCombatEngine().getCustomData().containsKey(fightersKey)) {
                firstTime = true;
            }

            if (ship.isHulk()) {
                return;
            }

            float maxRangeBonus = ship.getCollisionRadius() * 1f;
            float jitterRangeBonus = 5f + jitterLevel * maxRangeBonus;

            if (firstTime) {
                Global.getSoundPlayer().playSound("system_phase_skimmer", 1f, 0.5f, ship.getLocation(), ship.getVelocity());
            }

            ship.setJitter(KEY_JITTER, JITTER_COLOR, jitterLevel, 10, 0f, jitterRangeBonus);
            if (ship.isAlive()) {
                ship.setPhased(true);
            }

            if (state == State.IN) {
                float alpha = 1f - effectLevel * 0.5f;
                ship.setExtraAlphaMult(alpha);
            }

            if (effectLevel == 1) {
                if (ship.getWing() != null && ship.getWing().getSource() != null) {
                    ship.getWing().getSource().makeCurrentIntervalFast();
                    ship.getWing().getSource().land(ship);
                } else {
                    ship.setExtraAlphaMult(1);
                }
            }

        }
    }

    @Override
    public void unapply(MutableShipStatsAPI stats, String id) {
        ShipAPI ship = null;
        if (stats.getEntity() instanceof ShipAPI) {
            ship = (ShipAPI) stats.getEntity();
        } else {
            return;
        }

        final String fightersKey = ship.getId() + "_recall_device";
        Global.getCombatEngine().getCustomData().remove(fightersKey);

    }

    @Override
    public StatusData getStatusData(int index, State state, float effectLevel) {
        return null;
    }
}
