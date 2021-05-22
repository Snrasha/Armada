package src.data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipSystemAPI;
import com.fs.starfarer.api.combat.ShipSystemAPI.SystemState;
import com.fs.starfarer.api.combat.WeaponAPI;
import java.awt.Color;

public class PulseIndustry_autospin extends BaseHullMod {

    private static final float degrees_per_second = 8f;
    private float angle = 0.0f;
    private float chargeLevel = 0;

    public static final Color JITTER_COLOR = new Color(255, 50, 50, 75);
    public static final Color JITTER_UNDER_COLOR = new Color(255, 50, 50, 155);

    @Override
    public boolean isApplicableToShip(ShipAPI ship) {
        return true;
    }

    @Override
    public void advanceInCombat(ShipAPI ship, float amount) {

        super.advanceInCombat(ship, amount);

        
        CombatEngineAPI engine = Global.getCombatEngine();
        if (engine.isPaused()) {
            return;
        }
        if (ship.isHulk() && !ship.isAlive()) {
            return;
        }
        ShipSystemAPI system = ship.getSystem();

        // float effectLevel = system.getEffectLevel();
        if (system.isChargeup()) {
            chargeLevel += 1;
        } else if (!system.isActive() || system.isChargedown()) {
            if (chargeLevel > 0) {
                chargeLevel -= 0.2;
            }
        }
        

        if (chargeLevel > 0f) {

            float jitterLevel = chargeLevel / 150;
            if (system.getState() == SystemState.OUT) {
                jitterLevel *= jitterLevel;
            }
            float maxRangeBonus = 50f;
            float jitterRangeBonus = jitterLevel * maxRangeBonus;

            ship.setJitterUnder(this, JITTER_UNDER_COLOR, jitterLevel, 21, 0f, 3f + jitterRangeBonus);
            ship.setJitter(this, JITTER_COLOR, jitterLevel, 4, 0f, 0 + jitterRangeBonus);
        }
        float degrees = degrees_per_second + chargeLevel*2;
        angle = normalizeAngle(angle + (amount * (degrees)));
        ship.setFacing(-angle);
        for(WeaponAPI weapon : ship.getAllWeapons()){
            if(!weapon.isDecorative())
             weapon.setCurrAngle(0);

        }
       
    }

    public float normalizeAngle(float angleDeg) {
        return (angleDeg % 360f + 360f) % 360f;
    }

}
