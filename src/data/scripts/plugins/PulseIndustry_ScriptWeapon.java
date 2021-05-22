/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.data.scripts.plugins;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.EveryFrameWeaponEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipEngineControllerAPI;
import com.fs.starfarer.api.combat.ShipSystemAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import java.awt.Color;

import java.util.Iterator;

public class PulseIndustry_ScriptWeapon implements EveryFrameWeaponEffectPlugin {

    private float valuefra;
    private int blu;
    //  private int red;
    private int green;

    public PulseIndustry_ScriptWeapon() {
        valuefra = 0;
        // red = 255;
        blu = 0;
        green = 0;

    }

    public Color LerpColor(Color col1, Color col2, float amount) {

        return new Color(col1.getRed() * amount + col2.getRed() * (1 - amount),
                col1.getBlue() * amount + col2.getBlue() * (1 - amount),
                col1.getGreen() * amount + col2.getGreen() * (1 - amount));
    }

    @Override
    public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {
        if (engine.isPaused()) {
            return;
        }

        if (weapon == null || weapon.getShip() == null) {
            return;
        }

        ShipAPI ship = weapon.getShip();

        if (ship.isHulk() && !ship.isAlive()) {
            return;
        }

        ShipEngineControllerAPI engin = ship.getEngineController();

        boolean acc = engin.isAccelerating();
        boolean dec = engin.isDecelerating();
        boolean dacc = engin.isAcceleratingBackwards();

        if ((dacc || dec) && valuefra <= 1f) {
            valuefra += amount;
            if (valuefra > 1f) {
                valuefra = 1f;
            }
        } else if (acc) {
            valuefra = 0.4f;
        }

        Iterator<ShipEngineControllerAPI.ShipEngineAPI> iter = engin.getShipEngines().iterator();
        ShipEngineControllerAPI.ShipEngineAPI thruster;
        while (iter.hasNext()) {
            thruster = iter.next();
            if (thruster.isActive() && "pkbtop".equals(thruster.getStyleId())) {
                if (dec) {
                    engin.setFlameLevel(thruster.getEngineSlot(), valuefra);
                } else if (acc) {
                    engin.setFlameLevel(thruster.getEngineSlot(), 0.4f);
                }
                if (dacc) {
                    engin.setFlameLevel(thruster.getEngineSlot(), valuefra);
                }
            }
        }

        int amt = (int) (amount * 200);

        if (weapon.getSprite() == null) {
            return;
        }

        if (ship.getShield() != null && ship.getShield().isOn() && green < 255) {
            green += amt;
            if (green > 255) {
                green = 255;
            }
        } else if (green > 0) {
            green -= amt;
            if (green < 0) {
                green = 0;
            }
        }
        ShipSystemAPI system = ship.getSystem();
        if (system != null && system.isActive() && blu < 255) {
            blu += amt;
            if (blu > 255) {
                blu = 255;
            }
        } else if (blu > 0) {
            blu -= amt;
            if (blu < 0) {
                blu = 0;
            }
        }

        Color color = weapon.getSprite().getColor();
        if (color.getBlue() != blu || color.getGreen() != green) {
            weapon.getSprite().setColor(new Color(0, green, blu, 200));
        }
    }

}
