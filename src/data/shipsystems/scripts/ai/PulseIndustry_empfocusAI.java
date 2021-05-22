package src.data.shipsystems.scripts.ai;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipSystemAIScript;
import com.fs.starfarer.api.combat.ShipSystemAPI;
import com.fs.starfarer.api.combat.ShipwideAIFlags;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.combat.WeaponAPI.WeaponSize;
import com.fs.starfarer.api.util.Misc;
import org.lwjgl.util.vector.Vector2f;

public class PulseIndustry_empfocusAI implements ShipSystemAIScript {

    private CombatEngineAPI engine;
    private ShipAPI ship;
    private ShipSystemAPI system;
    private WeaponAPI largeLeft;
    private WeaponAPI largeRight;

    private float tracker=0;
    private float trackermax=0;

    @Override
    public void advance(float amount, Vector2f missileDangerDir, Vector2f collisionDangerDir, ShipAPI target) {
        if (engine == null) {
            return;
        }

        if (engine.isPaused()) {
            return;
        }

        tracker += amount;

        if (tracker > trackermax) {
            tracker = 0;
            if (ship.getFluxTracker().isOverloadedOrVenting()) {
                return;
            }

            if (!system.isOn() && target!=null) {
                float dist=Misc.getDistance(ship.getLocation(), target.getLocation()) -ship.getCollisionRadius() + target.getCollisionRadius();
              //  Global.getCombatEngine().getCombatUI().addMessage(0, ""+(dist)+";"+(largeLeft.getRange())+";"+(largeRight.getRange()+";"+(largeLeft.isFiring())+";"+(largeRight.isFiring())));

                if(dist<1500 && ((largeLeft!=null && largeLeft.isFiring())|| (largeRight!=null && largeRight.isFiring()))){
                      ship.useSystem();
                      tracker-=5;
                }
                /*
Global.getCombatEngine().getCombatUI().addMessage(0, ""+(PulseIndustry_empfocus.findTarget(ship) != null)+";"+(largeLeft.isFiring())+";"+(largeRight.isFiring()));
                if (PulseIndustry_empfocus.findTarget(ship) != null && ((largeLeft==null && largeRight==null)
                        || (largeLeft!=null && largeLeft.) 
                        || (largeRight!=null && largeRight.isFiring()))){
                    
                    ship.useSystem();
                }*/
            }
        }
    }

    @Override
    public void init(ShipAPI ship, ShipSystemAPI system, ShipwideAIFlags flags, CombatEngineAPI engine) {
        this.ship = ship;
        this.system = system;
        this.engine = engine;
        this.tracker = 0;
        this.trackermax = 0.2f;

        for (WeaponAPI weapon : ship.getAllWeapons()) {
            if(weapon.getSlot().getId().equals("LARGE_0")){
                largeLeft = weapon;
            }
            if(weapon.getSlot().getId().equals("LARGE_1")){
                largeRight = weapon;
            }
        }
      
        if(largeLeft!=null && largeLeft.hasAIHint(WeaponAPI.AIHints.PD)){
            largeLeft=null;
        }
        if(largeRight!=null &&  largeRight.hasAIHint(WeaponAPI.AIHints.PD)){
            largeRight=null;
        }
    }
}
