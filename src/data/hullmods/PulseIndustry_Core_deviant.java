package src.data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import java.util.HashMap;
import java.util.Map;

public class PulseIndustry_Core_deviant extends BaseHullMod {

   	public static final float SHIELD_UPKEEP_BONUS = 10f;
	private static final float FLUX_HARDFLUX_DISSIPATION = 5f;
        private static final float RANGE_ADD = 50f;
	public static final float DAMAGE_MULT = 1f;
        private static final Map RADIUS_MULT = new HashMap();
	static {
		RADIUS_MULT.put(HullSize.FRIGATE, 1.1f);
		RADIUS_MULT.put(HullSize.DESTROYER, 1.2f);
		RADIUS_MULT.put(HullSize.CRUISER,1.3f);
		RADIUS_MULT.put(HullSize.CAPITAL_SHIP, 1.4f);
	}
        private static final String SO = "safetyoverrides";

           @Override
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		stats.getShieldUpkeepMult().modifyMult(id, 1f - SHIELD_UPKEEP_BONUS * 0.01f);
                stats.getHardFluxDissipationFraction().modifyMult(id, FLUX_HARDFLUX_DISSIPATION/100);
                stats.getEnergyWeaponRangeBonus().modifyFlat(id, RANGE_ADD);
                stats.getBallisticWeaponRangeBonus().modifyFlat(id, RANGE_ADD);
                stats.getDynamic().getStat(Stats.EXPLOSION_DAMAGE_MULT).modifyMult(id, (Float)RADIUS_MULT.get(hullSize));
		stats.getDynamic().getStat(Stats.EXPLOSION_RADIUS_MULT).modifyMult(id, (Float)RADIUS_MULT.get(hullSize));
	}
	
           @Override
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + (int) SHIELD_UPKEEP_BONUS + "%";
                if (index == 1) return "" + (int) FLUX_HARDFLUX_DISSIPATION + "%";
		if (index == 2) return "" + (int) RANGE_ADD;
                if (index == 3) return "" + (((int) ((Float)RADIUS_MULT.get(HullSize.FRIGATE)*100))- 100) + "%";
                if (index == 4) return "" + (((int) ((Float)RADIUS_MULT.get(HullSize.DESTROYER)*100))- 100)  + "%";
                if (index == 5) return "" + (((int) ((Float)RADIUS_MULT.get(HullSize.CRUISER)*100))- 100)  + "%";
                if (index == 6) return "" + (((int) ((Float)RADIUS_MULT.get(HullSize.CAPITAL_SHIP)*100))- 100) + "%";
		return null;
	}

           @Override
	public boolean isApplicableToShip(ShipAPI ship) {
		return ship != null && ship.getShield() != null;
	}
         @Override
    public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
        if (ship.getVariant().getHullMods().contains(SO)) {
            ship.getVariant().removeMod(SO);
        }
    }
}
