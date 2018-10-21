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

package me.bradleysteele.timedrewards.util;

/**
 * @author Bradley Steele
 */
public final class Permissions {

    private Permissions() {}

    private static final String PREFIX = "timedrewards";
    private static final String ADMIN_PREFIX = PREFIX + ".admin";

    // Admin Commands
    public static final String TR_RELOAD = ADMIN_PREFIX + ".reload";

}