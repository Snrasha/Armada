/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.data.scripts.plugins;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.EveryFrameWeaponEffectPlugin;
import com.fs.starfarer.api.combat.WeaponAPI;

public class PulseIndustry_minster_System implements EveryFrameWeaponEffectPlugin {

 //   private AnchoredEntity entity;

    @Override
    public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {
        if (engine.isPaused() || weapon.getShip() == null) {
            return;
        }
     /*   if(entity==null){
            entity = new AnchoredEntity(weapon.getShip(), weapon.getLocation());
        }*/
        int inc=0;
        for (WeaponAPI weapon1 : weapon.getShip().getAllWeapons()) {
            if (weapon1.isDecorative() && weapon!=weapon1) {
                float curr = weapon1.getCurrAngle();
                curr +=  amount * 10f* (inc%2==0?1:-1);

               // engine.spawnEmpArc(weapon.getShip() , weapon1.getLocation(), entity, entity, DamageType.KINETIC, 0, 0, 0,  "tachyon_lance_fire", 0, Color.BLUE, Color.CYAN);
               weapon1.setCurrAngle(curr);
                inc++;
            }
        }
    }

    
}
