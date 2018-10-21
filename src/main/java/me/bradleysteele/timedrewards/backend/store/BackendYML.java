/*
 * Copyright 2018 Bradley Steele
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.bradleysteele.timedrewards.backend.store;

import me.bradleysteele.timedrewards.backend.TRUserProfile;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

/**
 * @author Bradley Steele
 */
public class BackendYML extends Backend {

    private File folder;

    @Override
    public void onRegister() {
        folder = new File(plugin.getDataFolder(), "playerdata");

        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    @Override
    public TRUserProfile retrieve(UUID uuid) {
        YamlConfiguration config = retrieveConfiguration(uuid);

        if (config == null) {
            config = createConfiguration(uuid);
        }

        TRUserProfile profile = new TRUserProfile(uuid);
        ConfigurationSection section = config.getConfigurationSection("claim-times");

        for (String item : section.getKeys(false)) {
            profile.setClaimTime(item, section.getLong(item));
        }

        return profile;
    }

    @Override
    public void store(TRUserProfile profile) {
        UUID uuid = profile.getUUID();
        YamlConfiguration config = retrieveConfiguration(uuid);

        if (config == null) {
            config = createConfiguration(uuid);
        }

        for (Map.Entry<String, Long> entry : profile.getClaimTimes().entrySet()) {
            config.set("claim-times." + entry.getKey(), entry.getValue());
        }

        saveConfiguration(uuid, config);
    }

    /**
     * Attempts to retrieve the player's data file located.
     *
     * @param uuid user's unique identifier.
     * @return configuration if present, otherwise {@code null}.
     */
    public YamlConfiguration retrieveConfiguration(UUID uuid) {
        File file = new File(folder, uuid.toString() + ".yml");
        return file.exists() ? YamlConfiguration.loadConfiguration(file) : null;
    }

    /**
     * Creates and will override existing files for specified
     * player.
     *
     * @param uuid user's unique identifier.
     * @return the newly created .yml file.
     */
    public YamlConfiguration createConfiguration(UUID uuid) {
        File file = new File(folder, uuid.toString() + ".yml");

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set("uuid", uuid.toString());
        config.set("name", Bukkit.getOfflinePlayer(uuid).getName());
        config.createSection("claim-times");

        saveConfiguration(uuid, config);
        return config;
    }

    /**
     * @param uuid   player's unique identifier.
     * @param config configuration to be saved.
     */
    public void saveConfiguration(UUID uuid, YamlConfiguration config) {
        File file = new File(folder, uuid.toString() + ".yml");

        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getConsole().error("Failed to save " + uuid.toString() + "'s data file:");
            plugin.getConsole().exception(e);
        }
    }
}