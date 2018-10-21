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

package me.bradleysteele.timedrewards.resource.yml;

import com.google.common.collect.Lists;
import me.bradleysteele.commons.resource.type.ResourceYaml;
import me.bradleysteele.timedrewards.resource.ResourceType;
import me.bradleysteele.timedrewards.resource.Resources;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Bradley Steele
 */
public enum Config {

    CONFIG_VERSION("config-version", 1),

    MENU_CLOSE_ON_CLAIM("menu.close-on-claim", true),
    MENU_NPCS("menu.npcs", Collections.emptyList()),

    REWARDS_ENABLED("rewards.enabled", true),
    REWARDS_ALIASES("rewards.aliases", Collections.emptyList()),

    ;

    public static ResourceYaml getConfig() {
        return (ResourceYaml) Resources.get().getResource(ResourceType.CONFIG);
    }

    private final String path;
    private final Object def;

    /**
     * @param path the path to the setting.
     * @param def  default value of the setting.
     */
    Config(String path, Object def) {
        this.path = path;
        this.def = def;
    }

    /**
     * @return path of the setting in config.yml.
     */
    public String getPath() {
        return path;
    }

    /**
     * @return value of the path as a string, also replaces the {prefix} placeholder.
     */
    public String getAsString() {
        return getConfig().getString(path, String.valueOf(def));
    }

    /**
     * @return value of the path as a string list.
     */
    public List<String> getAsStringList() {
        List<String> list = Lists.newArrayList();

        if (getConfig().getConfiguration().isString(path)) {
            list.add(getAsString());
        } else {
            list.addAll(getConfig().getConfiguration().getStringList(path));
        }

        return list.stream()
                .map(s -> s.replace("{prefix}", Locale.PREFIX.getMessage().get(0)))
                .collect(Collectors.toList());
    }

    /**
     * @return value of the path as an int.
     */
    public int getAsInt() {
        return getConfig().getInt(path, Integer.parseInt(getAsString()));
    }

    /**
     * @return default value as an integer.
     */
    public int getAsIntDefault() {
        return Integer.parseInt(String.valueOf(def));
    }

    /**
     * @return value of the path as a short.
     */
    public short getAsShort() {
        return getConfig().getShort(path, Short.parseShort(getAsString()));
    }

    /**
     * @return value of the path as a double.
     */
    public double getAsDouble() {
        return getConfig().getDouble(path, Double.parseDouble(getAsString()));
    }

    /**
     * @return value of the path as a boolean.
     */
    public boolean getAsBoolean() {
        return getConfig().getBoolean(path, Boolean.parseBoolean(getAsString()));
    }

    /**
     * @return value of the path as a configuration section.
     */
    public ConfigurationSection getAsConfigurationSection() {
        return getConfig().getConfigurationSection(path);
    }
}