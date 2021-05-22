package src.data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.combat.ShipEngineControllerAPI;
import com.fs.starfarer.api.util.Misc;
import java.util.HashMap;
import java.util.Map;

public class PulseIndustry_Core extends BaseHullMod {

    private static final Map SPEED = new HashMap();

    static {
        SPEED.put(HullSize.FRIGATE, 50f);
        SPEED.put(HullSize.DESTROYER, 30f);
        SPEED.put(HullSize.CRUISER, 20f);
        SPEED.put(HullSize.CAPITAL_SHIP, 10f);
    }
    private static final float FLUX_SHUNT_DISSIPATION = 10f;
    private static final float FLUX_DISSIPATION_MULT = 1.5f;
   // private static final float OVERLOAD_TIME_MULT = 50f;
    private static final float RANGE_THRESHOLD = 450f;
    private static final float RANGE_MULT = 0.5f;
    private static final String SO = "safetyoverrides";

    @Override
    public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {

        stats.getMaxSpeed().modifyFlat(id, (Float) SPEED.get(hullSize));
        stats.getAcceleration().modifyFlat(id, (Float) SPEED.get(hullSize) * 2f);
        stats.getDeceleration().modifyFlat(id, (Float) SPEED.get(hullSize) * 2f);
        stats.getZeroFluxMinimumFluxLevel().modifyFlat(id, 2f); // set to two, meaning boost is always on 

        //stats.getOverloadTimeMod().modifyMult(id, OVERLOAD_TIME_MULT/100);
        stats.getHardFluxDissipationFraction().modifyMult(id, 0.1f);
        
        stats.getFluxDissipation().modifyMult(id, FLUX_DISSIPATION_MULT);
        stats.getVentRateMult().modifyMult(id, 0f);
        stats.getWeaponRangeThreshold().modifyFlat(id, RANGE_THRESHOLD);
        stats.getWeaponRangeMultPastThreshold().modifyMult(id, RANGE_MULT);
     	stats.getHardFluxDissipationFraction().modifyFlat(id, FLUX_SHUNT_DISSIPATION / 100f);

    }
    

    @Override
    public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
        if (ship.getVariant().getHullMods().contains(SO)) {
            ship.getVariant().removeMod(SO);
        }
    }

    @Override
    public void advanceInCombat(ShipAPI ship, float amount) {

        CombatEngineAPI engin = Global.getCombatEngine();
        if (engin.isPaused()) {
            return;
        }
        if (ship.isHulk() && !ship.isAlive()) {
            return;
        }

       
        ShipEngineControllerAPI engine = ship.getEngineController();

        engine.extendFlame(this, 0.25f, 0.25f, 0.25f);

    }

    @Override
    public String getDescriptionParam(int index, HullSize hullSize) {
        if (index == 0) {
            return "" + ((Float) SPEED.get(HullSize.FRIGATE)).intValue();
        }
        if (index == 1) {
            return "" + ((Float) SPEED.get(HullSize.DESTROYER)).intValue();
        }
        if (index == 2) {
            return "" + ((Float) SPEED.get(HullSize.CRUISER)).intValue();
        }
        if (index == 3) {
            return "" + ((Float) SPEED.get(HullSize.CAPITAL_SHIP)).intValue();
        }
        if (index == 4) {
            return Misc.getRoundedValue(FLUX_DISSIPATION_MULT);
        }
        if (index == 5) {
            return Float.toString(RANGE_THRESHOLD);
        }
        if (index == 6) {
            return Float.toString(FLUX_SHUNT_DISSIPATION)+"%";
        }
        if (index == 7) {
            return "98%";
        }
        return null;
    }
}
