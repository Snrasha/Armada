package src.data.shipsystems.scripts.ai;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipSystemAIScript;
import com.fs.starfarer.api.combat.ShipSystemAPI;
import com.fs.starfarer.api.combat.ShipwideAIFlags;
import com.fs.starfarer.api.util.IntervalUtil;
import org.lazywizard.lazylib.MathUtils;

import org.lwjgl.util.vector.Vector2f;

public class PulseIndustry_recalldeviceAI implements ShipSystemAIScript {

    private CombatEngineAPI engine;
    private ShipAPI ship;
    private ShipSystemAPI system;

    private final IntervalUtil tracker = new IntervalUtil(0.1f, 0.2f);

    @Override
    public void advance(float amount, Vector2f missileDangerDir, Vector2f collisionDangerDir, ShipAPI target) {
        if (engine == null) {
            return;
        }

        if (engine.isPaused()) {
            return;
        }

        tracker.advance(amount);

        if (tracker.intervalElapsed()) {
            if (ship.getFluxTracker().isOverloadedOrVenting() || ship.getEngineController().isDisabled()) {
                return;
            }

            if (!system.isOn()) {

                ShipAPI father = ship.getWing().getSourceShip();
                if (father != null) {
                    if(father.getVariant()!= null && father.getVariant().hasHullMod("converted_hangar")) return;

                    if ((ship.getMaxHitpoints() * 0.5f > ship.getHitpoints()) ||  (father.isPullBackFighters() && MathUtils.getDistance(ship, father) > 700)) {
                        ship.useSystem();
                    }
                }

            }
        }
    }

    @Override
    public void init(ShipAPI ship, ShipSystemAPI system, ShipwideAIFlags flags, CombatEngineAPI engine) {
        this.ship = ship;
        this.system = system;
        this.engine = engine;

    }
}
