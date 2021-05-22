package src.data.scripts.ai;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CollisionClass;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.GuidedMissileAI;
import com.fs.starfarer.api.combat.MissileAIPlugin;
import com.fs.starfarer.api.combat.MissileAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipCommand;
import java.awt.Color;

import java.util.List;

import org.lazywizard.lazylib.MathUtils;
import org.lazywizard.lazylib.VectorUtils;
import org.lazywizard.lazylib.combat.AIUtils;
import org.lwjgl.util.vector.Vector2f;
public class PulseIndustry_CustomAL implements MissileAIPlugin, GuidedMissileAI {

    private final float maxcooldown = 0.1f;
    private float cooldownInterval = 0;
    private final MissileAPI missile;
    private CombatEntityAPI target;

    public PulseIndustry_CustomAL(MissileAPI missile, ShipAPI launchingShip) {
        this.missile = missile;

        if (launchingShip.getShipTarget() != null && !launchingShip.getShipTarget().isHulk()) {
            target = launchingShip.getShipTarget();
        }
        if (target == null) {
            setTarget(findBestTarget(missile));
        }
    }

    @Override
    public void advance(float amount) {
         if (missile.isFizzling() || !missile.isArmed() || missile.isFading()|| ((missile.getMaxHitpoints()/2)>missile.getHitpoints())) {
                                   
            Vector2f zero = new Vector2f();
            Global.getCombatEngine().addSmoothParticle(missile.getLocation(), zero, 90, 0.5f, 1, Color.ORANGE);
            Global.getCombatEngine().addSmoothParticle(missile.getLocation(), zero, 30, 0.5f, 1, Color.WHITE);
            
             Global.getCombatEngine().removeEntity(missile);
            return;
        }

        missile.giveCommand(ShipCommand.ACCELERATE);

        if (target == null || (target instanceof ShipAPI && (((ShipAPI) target).isHulk())) || (missile.getOwner() == target.getOwner())
                    || !Global.getCombatEngine().isEntityInPlay(target)) {
            setTarget(findBestTarget(missile));
            if (target == null) {
                return;
            }
        }

        this.cooldownInterval+=amount;

        if (this.cooldownInterval>this.maxcooldown) {
            this.cooldownInterval=0;

            float courseCorrectingAngle = MathUtils.clampAngle(VectorUtils.getAngle(missile.getLocation(), target.getLocation())
                                                                       + MathUtils.getRandomNumberInRange(-20f, 20f));

            float clampFacing = MathUtils.clampAngle(missile.getFacing());

            
            float turnrate= this.missile.getTurnAcceleration();
            
                float tempVal;
                if (clampFacing < courseCorrectingAngle) {
                    tempVal = courseCorrectingAngle - clampFacing;

                    if (tempVal > 180) {
                        clampFacing -= turnrate;
                    } else {
                        clampFacing += turnrate;
                    }
                } else if (clampFacing > courseCorrectingAngle) {
                    tempVal = clampFacing - courseCorrectingAngle;

                    if (tempVal < 180) {

                        clampFacing -= turnrate;
                    } else {
                        clampFacing += turnrate;
                    }
                }
                if (MathUtils.getRandomNumberInRange(0, 5) != 0) {
                    clampFacing += MathUtils.getRandomNumberInRange(-10, 10);
                }

                missile.setFacing(clampFacing);
            
        }
    }

    @Override
    public CombatEntityAPI getTarget() {
        return target;
    }

    @Override
    public final void setTarget(CombatEntityAPI target) {
        this.target = target;
    }

    private static ShipAPI findBestTarget(MissileAPI missile) {
        ShipAPI closest = null;
        float distance, closestDistance = Float.MAX_VALUE;
        ShipAPI source=missile.getSource();
        if(source!=null){
            ShipAPI target=source.getShipTarget();
            if(target!=null){
                return target;
            }
        }

        List<ShipAPI> ships = AIUtils.getEnemiesOnMap(missile);
        int size = ships.size();
        for (int i = 0; i < size; i++) {
            ShipAPI tmp = ships.get(i);
            float mod = 0f;
            if (tmp.isFighter() || tmp.isDrone() || tmp.getCollisionClass() == CollisionClass.NONE) {
                mod = 4000f;
            }
            distance = MathUtils.getDistance(tmp, missile.getLocation()) + mod;
            if (distance < closestDistance) {
                closest = tmp;
                closestDistance = distance;
            }
        }

        return closest;
    }

}
