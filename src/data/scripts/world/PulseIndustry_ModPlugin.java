package src.data.scripts.world;

import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.PluginPick;
import com.fs.starfarer.api.campaign.CampaignPlugin;
import com.fs.starfarer.api.combat.MissileAIPlugin;
import com.fs.starfarer.api.combat.MissileAPI;
import com.fs.starfarer.api.combat.ShipAPI;

import src.data.scripts.ai.*;

public class PulseIndustry_ModPlugin extends BaseModPlugin {

    private final String IDAL = "PulseIndustry_al_shot";

    @Override
    public PluginPick<MissileAIPlugin> pickMissileAI(MissileAPI missile, ShipAPI launchingShip) {

        switch (missile.getProjectileSpecId()) {

            case IDAL:
                return new PluginPick<MissileAIPlugin>(new PulseIndustry_CustomAL(missile, launchingShip), CampaignPlugin.PickPriority.MOD_SET);
            default:
                return null;
        }
    }
}
