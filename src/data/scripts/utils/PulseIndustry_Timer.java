
package src.data.scripts.utils;

import org.lwjgl.util.vector.Vector2f;


/*

PulseIndustry_Timer contains a timer and the position of where the emp need to spawn.
*/
public class PulseIndustry_Timer {

    private int value;
    private final int valuemax;
    private final Vector2f[] weaponPos;
   
    public PulseIndustry_Timer(int max,Vector2f[] weaponPos){
        value=0;
        this.valuemax=Math.abs(max);
        this.weaponPos=weaponPos;
    }
    
    public void advance(){
        value++;
        if(value> this.valuemax){
            value=0;
        }
    }
    
    public boolean isElapsed(){
        return value==0;
    }
    
    public Vector2f[] getPosition(){
        return weaponPos;
    }
}
