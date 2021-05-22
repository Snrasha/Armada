package src.data.shipsystems.scripts.ai;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipSystemAIScript;
import com.fs.starfarer.api.combat.ShipSystemAPI;
import com.fs.starfarer.api.combat.ShipwideAIFlags;
import com.fs.starfarer.api.util.IntervalUtil;
import java.util.List;
import java.util.Random;
import org.lazywizard.lazylib.combat.AIUtils;
import org.lwjgl.util.vector.Vector2f;

public class PulseIndustry_LocustAI implements ShipSystemAIScript {

    private CombatEngineAPI engine;
    private ShipAPI ship;
    private ShipSystemAPI system;
   // private static final float RANGE = 800f;

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
            if (ship.getFluxTracker().isOverloadedOrVenting()) {
                return;
            }
            
            if (!system.isOn()) {
                List<ShipAPI> ships = AIUtils.getNearbyEnemies(ship, ship.getAllWeapons().get(0).getRange());// RANGE
                if (!ships.isEmpty()) {
                    Random rand = new Random();
                    int max = 5 - ships.size();
                    boolean flag = false;
                    if (max < 1) {
                        flag = true;
                    }
                    else if (rand.nextInt(max) == 0) {
                        flag = true;
                    }
                    if (flag) {
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
