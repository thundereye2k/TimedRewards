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

package me.bradleysteele.timedrewards.backend;

import me.bradleysteele.commons.register.store.BStore;
import me.bradleysteele.timedrewards.backend.store.Backend;
import me.bradleysteele.timedrewards.backend.store.BackendYML;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

/**
 * @author Bradley Steele
 */
public class StoreTRUserProfile extends BStore<TRUserProfile> {

    private static final StoreTRUserProfile instance = new StoreTRUserProfile();

    public static StoreTRUserProfile get() {
        return instance;
    }

    private Backend backend;

    @Override
    public void onRegister() {
        setBackend(new BackendYML());
    }

    public TRUserProfile retrieve(UUID uuid) {
        TRUserProfile profile;

        // Check to see if profiles exist in store, otherwise retrieve from
        // backend store and cache in local store.
        if (this.exists(uuid)) {
            profile = super.retrieve(uuid);
        } else {
            profile = backend.retrieve(uuid);
            this.store(uuid, profile);
        }

        return profile;
    }

    public TRUserProfile retrieve(OfflinePlayer player) {
        return retrieve(player.getUniqueId());
    }

    /**
     * @return backend responsible for user profiles.
     */
    public Backend getBackend() {
        return backend;
    }

    /**
     * @param backend the backend responsible for handling
     *                user profiles.
     */
    public void setBackend(Backend backend) {
        this.backend = backend;

        plugin.register(backend);
    }
}