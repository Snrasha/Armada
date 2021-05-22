package src.data.scripts.utils;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.ShipAPI;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.lwjgl.util.vector.Vector2f;

public class PulseIndustry_weaponListPosition {

    private static final Map<String, Vector2f[]> WEAPONLIST = new HashMap<>();

    public static Vector2f[] posWeapon(ShipAPI ship) {

        if (WEAPONLIST.isEmpty()) {
            try {

                JSONObject jsonObject = Global.getSettings().loadJSON("data/config/weapon_position.json");
                if (jsonObject == null) {
                    return null;
                }

                Iterator<String> iterKeys = jsonObject.keys();
                while (iterKeys.hasNext()) {

                    String hullid = iterKeys.next();

                    JSONArray array = jsonObject.getJSONArray(hullid);
                    if (array == null) {
                        continue;
                    }
                    int len = array.length();
                    if (len % 2 == 0) {
                        Vector2f[] position = new Vector2f[len / 2];
                        for (int i = 0; i < (len / 2); i++) {
                            position[i] = new Vector2f(array.getInt(i), array.getInt(i + 1));
                        }
                        WEAPONLIST.put(hullid, position);
                    }
                }
            } catch (IOException | JSONException ex) {
                return null;
            }
        }

        return WEAPONLIST.get(ship.getHullSpec().getHullId());
    }
}
