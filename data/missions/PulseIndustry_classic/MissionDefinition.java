package data.missions.PulseIndustry_classic;

import com.fs.starfarer.api.fleet.FleetGoal;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.fleet.FleetMemberType;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import com.fs.starfarer.api.mission.FleetSide;
import com.fs.starfarer.api.mission.MissionDefinitionAPI;
import com.fs.starfarer.api.mission.MissionDefinitionPlugin;

public class MissionDefinition implements MissionDefinitionPlugin {

    @Override
    public void defineMission(MissionDefinitionAPI api) {

        // Set up the fleets
        api.initFleet(FleetSide.PLAYER, "HSS", FleetGoal.ATTACK, false);
        api.initFleet(FleetSide.ENEMY, "ISS", FleetGoal.ATTACK, true);

        // Set a blurb for each fleet
        api.setFleetTagline(FleetSide.PLAYER, Factions.TRITACHYON);
        api.setFleetTagline(FleetSide.ENEMY, Factions.INDEPENDENT);

        // These show up as items in the bulleted list under 
        // "Tactical Objectives" on the mission detail screen
        api.addBriefingItem("Win.");

        // Set up the player's fleet
        api.addToFleet(FleetSide.PLAYER, "PulseIndustry_cuo_Basic", FleetMemberType.SHIP, true);
        api.addToFleet(FleetSide.PLAYER, "PulseIndustry_bignouf_Strike", FleetMemberType.SHIP, true);
        api.addToFleet(FleetSide.PLAYER, "PulseIndustry_malice_Attack", FleetMemberType.SHIP, true);
        api.addToFleet(FleetSide.PLAYER, "PulseIndustry_athenia_Support", FleetMemberType.SHIP, true);
        api.addToFleet(FleetSide.PLAYER, "PulseIndustry_lodium_Balance", FleetMemberType.SHIP, true);
        api.addToFleet(FleetSide.PLAYER, "PulseIndustry_minster_Standard", FleetMemberType.SHIP, true);

        FleetMemberAPI fleetMember;
        fleetMember = api.addToFleet(FleetSide.ENEMY, "dominator_Outdated", FleetMemberType.SHIP, "Rhinoceros", false);
        fleetMember.getCaptain().setPersonality("aggressive");
        api.addToFleet(FleetSide.ENEMY, "astral_Strike", FleetMemberType.SHIP, "TTS Ephemeral", false); 

        api.addToFleet(FleetSide.ENEMY, "enforcer_Balanced", FleetMemberType.SHIP, "Bully", false);
        api.addToFleet(FleetSide.ENEMY, "enforcer_Balanced", FleetMemberType.SHIP, "Bruiser", false);
        api.addToFleet(FleetSide.ENEMY, "condor_Support", FleetMemberType.SHIP, "Nidus", false);
        api.addToFleet(FleetSide.ENEMY, "condor_Strike", FleetMemberType.SHIP, "Nexus", false);
        api.addToFleet(FleetSide.ENEMY, "condor_Strike", FleetMemberType.SHIP, "Eyrie", false);
        api.addToFleet(FleetSide.ENEMY, "condor_Attack", FleetMemberType.SHIP, "Nidus", false);
        api.addToFleet(FleetSide.ENEMY, "hammerhead_Balanced", FleetMemberType.SHIP, "ISS Argentum", false);
        api.addToFleet(FleetSide.ENEMY, "buffalo2_FS", FleetMemberType.SHIP, false);
        api.addToFleet(FleetSide.ENEMY, "buffalo2_FS", FleetMemberType.SHIP, false);
        api.addToFleet(FleetSide.ENEMY, "hound_Standard", FleetMemberType.SHIP, "Rex", false);
        api.addToFleet(FleetSide.ENEMY, "hound_Standard", FleetMemberType.SHIP, "Spot", false);
        api.addToFleet(FleetSide.ENEMY, "hound_Standard", FleetMemberType.SHIP, "Lucky", false);
        api.addToFleet(FleetSide.ENEMY, "hound_Standard", FleetMemberType.SHIP, "Rusty", false);
        api.addToFleet(FleetSide.ENEMY, "lasher_CS", FleetMemberType.SHIP, "Overseer", false);
        api.addToFleet(FleetSide.ENEMY, "lasher_CS", FleetMemberType.SHIP, "Taskmaster", false);
        // Set up the map.

        // Set up the map.
        float width = 24000f;
        float height = 18000f;
        api.initMap((float) -width / 2f, (float) width / 2f, (float) -height / 2f, (float) height / 2f);

        float minX = -width / 2;
        float minY = -height / 2;

        // All the addXXX methods take a pair of coordinates followed by data for
        // whatever object is being added.
        // Add two big nebula clouds
        api.addNebula(minX + width * 0.66f, minY + height * 0.5f, 2000);
        api.addNebula(minX + width * 0.25f, minY + height * 0.6f, 1000);
        api.addNebula(minX + width * 0.25f, minY + height * 0.4f, 1000);

        // And a few random ones to spice up the playing field.
        for (int i = 0; i < 5; i++) {
            float x = (float) Math.random() * width - width / 2;
            float y = (float) Math.random() * height - height / 2;
            float radius = 100f + (float) Math.random() * 400f;
            api.addNebula(x, y, radius);
        }

        // add objectives
        api.addObjective(minX + width * 0.25f + 2000f, minY + height * 0.5f,
                "sensor_array");
        api.addObjective(minX + width * 0.75f - 2000f, minY + height * 0.5f,
                "comm_relay");
        api.addObjective(minX + width * 0.33f + 2000f, minY + height * 0.4f,
                "nav_buoy");
        api.addObjective(minX + width * 0.66f - 2000f, minY + height * 0.6f,
                "nav_buoy");

        api.addAsteroidField(-(minY + height), minY + height, -45, 2000f,
                20f, 70f, 100);

        api.addPlanet(0, 0, 400f, "barren", 200f, true);
        api.addRingAsteroids(0, 0, 30, 32, 32, 48, 200);
    }

}
