package src.data.shipsystems.scripts;


import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipSystemAPI;

import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;
import java.awt.Color;
import org.lazywizard.lazylib.combat.AIUtils;

public class PulseIndustry_Locuststats extends BaseShipSystemScript {

    public static final float DISRUPTION_DUR = 1f;
    public static final float MIN_DISRUPTION_RANGE = 800f;

    public static final Color OVERLOAD_COLOR = new Color(255, 155, 255, 255);

    public static final Color JITTER_COLOR = new Color(255, 155, 255, 75);
    public static final Color JITTER_UNDER_COLOR = new Color(255, 155, 255, 155);

    @Override
    public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {
        ShipAPI ship = null;
        if (stats.getEntity() instanceof ShipAPI) {
            ship = (ShipAPI) stats.getEntity();
        } else {
            return;
        }

        if (effectLevel > 0f) {

            float jitterLevel = effectLevel / 4;
            if (state == State.OUT) {
                jitterLevel *= jitterLevel;
            }
            float maxRangeBonus = 50f;
            float jitterRangeBonus = jitterLevel * maxRangeBonus;

            ship.setJitterUnder(this, JITTER_UNDER_COLOR, jitterLevel, 21, 0f, 3f + jitterRangeBonus);
            ship.setJitter(this, JITTER_COLOR, jitterLevel, 4, 0f, 0 + jitterRangeBonus);
        }
    }

    @Override
    public void unapply(MutableShipStatsAPI stats, String id) {
    }

    @Override
    public StatusData getStatusData(int index, State state, float effectLevel) {
        return null;
    }

    @Override
    public boolean isUsable(ShipSystemAPI system, ShipAPI ship) {
        if(ship!=null){
            return !AIUtils.getNearbyEnemies(ship,ship.getAllWeapons().get(0).getRange()).isEmpty();
        }
        return AIUtils.getNearbyEnemies(ship, MIN_DISRUPTION_RANGE).isEmpty();
    }

    @Override
    public String getInfoText(ShipSystemAPI system, ShipAPI ship) {
        if (system.isOutOfAmmo()) {
            return null;
        }
        if (system.getState() != ShipSystemAPI.SystemState.IDLE) {
            return null;
        }
        return super.getInfoText(system, ship);
    }
}
