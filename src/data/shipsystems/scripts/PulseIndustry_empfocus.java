package src.data.shipsystems.scripts;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseEveryFrameCombatPlugin;
import com.fs.starfarer.api.combat.DamageType;
import com.fs.starfarer.api.combat.MissileAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipSystemAPI;
import com.fs.starfarer.api.combat.ShipwideAIFlags;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.combat.WeaponAPI.WeaponSize;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;
import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.util.Misc;
import java.awt.Color;
import java.util.List;
import org.lazywizard.lazylib.combat.AIUtils;

public class PulseIndustry_empfocus extends BaseShipSystemScript {
    public static final float DISRUPTION_DUR = 1f;
    public static final float MIN_DISRUPTION_RANGE = 1500f;
    public static final float MAX_CORRUPTION_RANGE = 500f;

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

        float jitterLevel = effectLevel;
        if (state == State.OUT) {
            jitterLevel *= jitterLevel;
        }
        float maxRangeBonus = 50f;
        float jitterRangeBonus = jitterLevel * maxRangeBonus;


        ship.setJitterUnder(this, JITTER_UNDER_COLOR, jitterLevel, 21, 0f, 3f + jitterRangeBonus);
        ship.setJitter(this, JITTER_COLOR, jitterLevel, 4, 0f, 0 + jitterRangeBonus);

        String targetKey = ship.getId() + "_acausal_target";
        Object foundTarget = Global.getCombatEngine().getCustomData().get(targetKey);
        if (state == State.IN) {
            if (foundTarget == null) {
                ShipAPI target = findTarget(ship);
                if (target != null) {
                    Global.getCombatEngine().getCustomData().put(targetKey, target);
                }
            }
        } else if (effectLevel >= 1) {
            if (foundTarget instanceof ShipAPI) {
                ShipAPI target = (ShipAPI) foundTarget;
                if (target.getFluxTracker().isOverloadedOrVenting()) {
                    target = ship;
                    //ship.getAllWings().get(0).getSpec().getOpCost(ship.getMutableStats());
                    //ship.getAllWings().get(0).getWingMembers().add(ship)
                     //       Global.getCombatEngine().
                }
                applyEffectToTarget(ship, target);
                for (WeaponAPI weapon1 : ship.getAllWeapons()) {
                    if (weapon1.isDecorative() && weapon1.getSize().equals(WeaponSize.LARGE)) {
                        Global.getCombatEngine().spawnEmpArc(weapon1.getShip(), weapon1.getLocation(), target, target, DamageType.ENERGY, 1	,700,10000f, "tachyon_lance_fire", 10f, JITTER_UNDER_COLOR,JITTER_COLOR);
                        
                    }
                }
            }
        } else if (state == State.OUT && foundTarget != null) {
            Global.getCombatEngine().getCustomData().remove(targetKey);
        }
    }


    public static ShipAPI findTarget(ShipAPI ship) {
        boolean player = ship == Global.getCombatEngine().getPlayerShip();
        ShipAPI target = ship.getShipTarget();
        if (target != null) {
            float dist = Misc.getDistance(ship.getLocation(), target.getLocation());
            float radSum = ship.getCollisionRadius() + target.getCollisionRadius();
            if (dist > MIN_DISRUPTION_RANGE + radSum) {
                target = null;
            }
        } else {
            if (target == null || target.getOwner() == ship.getOwner()) {
                if (player) {
                    target = Misc.findClosestShipEnemyOf(ship, ship.getMouseTarget(), ShipAPI.HullSize.FIGHTER, MIN_DISRUPTION_RANGE, true);
                } else {
                    Object test = ship.getAIFlags().getCustom(ShipwideAIFlags.AIFlags.MANEUVER_TARGET);
                    if (test instanceof ShipAPI) {
                        target = (ShipAPI) test;
                        float dist = Misc.getDistance(ship.getLocation(), target.getLocation());
                        float radSum = ship.getCollisionRadius() + target.getCollisionRadius();
                        if (dist > MIN_DISRUPTION_RANGE + radSum) {
                            target = null;
                        }
                    }
                }
            }
            if (target == null) {
                target = Misc.findClosestShipEnemyOf(ship, ship.getLocation(), ShipAPI.HullSize.FIGHTER, MIN_DISRUPTION_RANGE, true);
            }
        }
        if (target == null || target.getFluxTracker().isOverloadedOrVenting()) {
            target = ship;
        }

        return target;
    }

    protected void applyEffectToTarget(final ShipAPI ship, final ShipAPI target) {
        if (target.getFluxTracker().isOverloadedOrVenting()) {
            return;
        }
        if (target == ship) {
            return;
        }

        target.setOverloadColor(OVERLOAD_COLOR);
        target.getFluxTracker().beginOverloadWithTotalBaseDuration(DISRUPTION_DUR);
        //target.getEngineController().forceFlameout(true);

        if (target.getFluxTracker().showFloaty()
                || ship == Global.getCombatEngine().getPlayerShip()
                || target == Global.getCombatEngine().getPlayerShip()) {
            target.getFluxTracker().playOverloadSound();
            target.getFluxTracker().showOverloadFloatyIfNeeded("System Disruption!", OVERLOAD_COLOR, 4f, true);
        }

        Global.getCombatEngine().addPlugin(new BaseEveryFrameCombatPlugin() {
            @Override
            public void advance(float amount, List<InputEventAPI> events) {
                if (!target.getFluxTracker().isOverloadedOrVenting()) {
                    target.resetOverloadColor();
                    Global.getCombatEngine().removePlugin(this);
                    List<ShipAPI> othersTarget=AIUtils.getNearbyAllies(target, MAX_CORRUPTION_RANGE+target.getCollisionRadius());
                    for(ShipAPI other: othersTarget){
                        Global.getCombatEngine().spawnEmpArc(ship, target.getLocation(), target, other, DamageType.ENERGY, 1	,700,10000f, "tachyon_lance_fire", 1f, JITTER_UNDER_COLOR,JITTER_COLOR);
                    }
                    List<MissileAPI> othersMissiles=AIUtils.getNearbyEnemyMissiles(target, MAX_CORRUPTION_RANGE+target.getCollisionRadius());
                    for(MissileAPI other: othersMissiles){
                        Global.getCombatEngine().spawnEmpArc(ship, target.getLocation(), target, other, DamageType.ENERGY, 1	,700,10000f, "tachyon_lance_fire", 1f, JITTER_UNDER_COLOR,JITTER_COLOR);
                    }
                }
            }
        });
    }

    @Override
    public StatusData getStatusData(int index, State state, float effectLevel) {
        /*float percent = (1f - ENERGY_DAM_PENALTY_MULT) * 100;
		if (index == 0 && percent > 0) {
			return new StatusData((int)percent + "% less energy damage", false);
		}*/
        return null;
    }

    @Override
    public String getInfoText(ShipSystemAPI system, ShipAPI ship) {
        if (system.isOutOfAmmo()) {
            return null;
        }
        if (system.getState() != ShipSystemAPI.SystemState.IDLE) {
            return null;
        }

        ShipAPI target = findTarget(ship);
        if (target != null && target != ship) {
            return "READY";
        }
        if ((target == null || target == ship) && ship.getShipTarget() != null) {
            return "OUT OF RANGE";
        }
        return "NO TARGET";
        //return super.getInfoText(system, ship);
    }

    @Override
    public boolean isUsable(ShipSystemAPI system, ShipAPI ship) {
        ShipAPI target = findTarget(ship);
        return target != null && target != ship;
        //return super.isUsable(system, ship);
    }

}
