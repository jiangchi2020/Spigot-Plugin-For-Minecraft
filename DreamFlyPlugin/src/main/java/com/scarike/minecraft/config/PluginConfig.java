package com.scarike.minecraft.config;

import org.bukkit.Location;
import org.bukkit.World;

public class PluginConfig {
    public long ticks_to_dream;
    public long interval_to_rand;
    public long max_dream_ticks;
    public int probability_to_dream;

    public World working_world = null;
    public WakeCondition wake_condition = new WakeCondition();

    public static class WakeCondition {
        public int distance;
        public int min_y;
        public int max_y;
        public boolean world_change;
        public boolean cmd_force;

        @Override
        public String toString() {
            return "WakeCondition{" +
                    "\n\t\tdistance=" + distance +
                    "\n\t\tmin_y=" + min_y +
                    "\n\t\tmax_y=" + max_y +
                    "\n\t\tworld_change=" + world_change +
                    "\n\t\tcmd_force=" + cmd_force +
                    "\n\t}";
        }
    }

    public Tips tips = new Tips();

    public static class Tips {
        public Title player_in_dream = new Title();
        public Title player_wake = new Title();

        public String progress_bar_title;

        @Override
        public String toString() {
            return "Tips{" +
                    "\n\t\tplayer_in_dream=" + player_in_dream +
                    "\n\t\tplayer_wake=" + player_wake +
                    "\n\t\tprogress_bar_title='" + progress_bar_title + '\'' +
                    "\n\t}";
        }

        public static class Title {
            public String title;
            public String subtitle;

            @Override
            public String toString() {
                return "Title{" +
                        "\n\t\t\ttitle='" + title + '\'' +
                        "\n\t\t\tsubtitle='" + subtitle + '\'' +
                        "\n\t\t}";
            }
        }
    }

    public boolean inRange(Location spawn, Location local) {
        if (wake_condition.distance > 0) {
            if (Math.sqrt(Math.pow(spawn.getX() - local.getX(), 2) + Math.pow(spawn.getZ() - local.getZ(), 2)) > wake_condition.distance) {
                return false;
            }
        }
        return !(local.getY() < wake_condition.min_y) && !(local.getY() > wake_condition.max_y);
    }

    @Override
    public String toString() {
        return "PluginConfig{" +
                "\n\tticks_to_dream=" + ticks_to_dream +
                "\n\tinterval_to_rand=" + interval_to_rand +
                "\n\tprobability_to_dream=" + probability_to_dream +
                "\n\tmax_dream_ticks=" + max_dream_ticks +
                "\n\tworking_world=" + working_world +
                "\n\twake_condition=" + wake_condition +
                "\n\ttips=" + tips +
                "\n}";
    }
}
