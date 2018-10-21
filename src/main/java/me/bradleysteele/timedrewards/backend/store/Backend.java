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

import me.bradleysteele.commons.register.store.BStore;
import me.bradleysteele.timedrewards.backend.TRUserProfile;

import java.util.UUID;

/**
 * @author Bradley Steele
 */
public abstract class Backend extends BStore<TRUserProfile> {

    /**
     * Retrieves the user's stored data and wraps it in
     * the {@link TRUserProfile} class.
     *
     * @param uuid user's unique id.
     * @return user profile.
     */
    public abstract TRUserProfile retrieve(UUID uuid);

    /**
     * @param profile user profile to store.
     */
    public abstract void store(TRUserProfile profile);

}
