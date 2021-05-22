package src.data.shipsystems.scripts.ai;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.MissileAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipSystemAIScript;
import com.fs.starfarer.api.combat.ShipSystemAPI;
import com.fs.starfarer.api.combat.ShipwideAIFlags;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.lazywizard.lazylib.MathUtils;

import org.lazywizard.lazylib.combat.AIUtils;
import org.lwjgl.util.vector.Vector2f;

public class PulseIndustry_EmpAI implements ShipSystemAIScript {

    private CombatEngineAPI engine;
    private ShipAPI ship;
    private ShipSystemAPI system;
    
    private static final Map THREATRANGE = new HashMap();
    static {
        THREATRANGE.put(ShipAPI.HullSize.FRIGATE, 600f);
        THREATRANGE.put(ShipAPI.HullSize.DESTROYER, 800f);
        THREATRANGE.put(ShipAPI.HullSize.CRUISER, 900f);
        THREATRANGE.put(ShipAPI.HullSize.CAPITAL_SHIP, 1000f);
    }
       private float tracker = 0;
       private float trackermax = 0;
    @Override
    public void advance(float amount, Vector2f missileDangerDir, Vector2f collisionDangerDir, ShipAPI target) {
        if (engine == null) {
            return;
        }

        if (engine.isPaused()) {
            return;
        }

        tracker+=amount;

        if (tracker>trackermax) {
            tracker=0;
            if (ship.getFluxTracker().isOverloadedOrVenting()) {
                return;
            }

            if (!system.isOn()) {

                
                Iterator<MissileAPI> itermissile = AIUtils.getNearbyEnemyMissiles(ship, (Float)THREATRANGE.get(ship.getHullSize())).iterator();
                MissileAPI missiletoTarget;
                
                
                int numberthreatFig=0;
                int numberthreatMis=0;
                

                while (itermissile.hasNext()) {
                    missiletoTarget = itermissile.next();
                    if (missiletoTarget != null && missiletoTarget.isArmed()) 
                            numberthreatMis++;
                }
               Iterator<ShipAPI> itership = AIUtils.getNearbyEnemies(ship, (Float)THREATRANGE.get(ship.getHullSize())).iterator();
               ShipAPI shiptoTarget;
               
               

               while (itership.hasNext()) {
                    shiptoTarget = itership.next();
                    if (shiptoTarget != null && shiptoTarget.isFighter()) 
                            numberthreatFig++;
                }
               if(numberthreatFig+numberthreatMis>=4 || (numberthreatMis>0 && MathUtils.getRandom().nextInt(5-numberthreatMis)==0) || (numberthreatFig>0 && MathUtils.getRandom().nextInt(5-numberthreatFig)==0)){
                   ship.useSystem();
               }

            }
        }
    }

    @Override
    public void init(ShipAPI ship, ShipSystemAPI system, ShipwideAIFlags flags, CombatEngineAPI engine) {
        this.ship = ship;
        this.system = system;
        this.engine = engine;
        this.tracker=0;
        this.trackermax=0.2f;
    }
}
