package src.data.scripts.weapons;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.EveryFrameWeaponEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import org.lazywizard.lazylib.FastTrig;
import org.lazywizard.lazylib.MathUtils;

/**
 *
 * Inspiration from Tartiflette Code
 */
public class PulseIndustry_tecoEffect implements EveryFrameWeaponEffectPlugin {

    private boolean runOnce = false;
    private ShipAPI ship;
    private float OFFSET = 0, ARC = 0;
    private WeaponAPI rshoulder;
    private WeaponAPI head;

    // The weapon who run is the head.
    @Override
    public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {

        if (!runOnce) {
            runOnce = true;
            ship = weapon.getShip();
            for (WeaponAPI w : ship.getAllWeapons()) {
                if (w.getSlot().getId().equals("RSHOULDER")) {
                    rshoulder = w;
                    OFFSET = w.getSlot().getAngle();
                    ARC = w.getSlot().getArc();
                } else if (w.getSlot().getId().equals("HEAD")) {
                    head = w;
                }
            }
        }

        if (engine.isPaused()) {
            return;
        }
        float aim = MathUtils.getShortestRotation(ship.getFacing(), rshoulder.getCurrAngle());

        aim -= OFFSET;
        aim = (aim + (ARC / 2)) / ARC;
        aim = SO(aim, 0, 1);
        aim = (aim * ARC) - ARC / 2;
        aim += OFFSET;
        head.setCurrAngle(ship.getFacing() + aim);

    }
    
    //From MagicLib magicanim, because i used just one function.
    public float SO (float x, float start, float end){
        return 0.5f - (float)( FastTrig.cos( Math.min( 1, Math.max( 0 , (x-start)*(1/(end-start)))) *MathUtils.FPI ) /2 );
    }
}
