package ch.cyberduck.core.features;

/*
 * Copyright (c) 2002-2016 iterate GmbH. All rights reserved.
 * https://cyberduck.io/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */

import ch.cyberduck.core.Credentials;
import ch.cyberduck.core.PasswordCallback;
import ch.cyberduck.core.Path;
import ch.cyberduck.core.VersioningConfiguration;
import ch.cyberduck.core.exception.BackgroundException;
import ch.cyberduck.core.exception.ConnectionCanceledException;

public interface Versioning {

    /**
     * Read configuration
     *
     * @param container Bucket
     * @return Current configuration
     * @throws BackgroundException API or network failure
     */
    VersioningConfiguration getConfiguration(Path container) throws BackgroundException;

    /**
     * Write configuration
     *
     * @param container     Bucket
     * @param prompt        Prompt for MFA token
     * @param configuration New configuration
     * @throws BackgroundException API or network failure
     */
    void setConfiguration(Path container, PasswordCallback prompt, VersioningConfiguration configuration) throws BackgroundException;

    /**
     * Restore this version
     *
     * @param file File
     * @throws BackgroundException API or network failure
     */
    void revert(Path file) throws BackgroundException;

    /**
     * Determine if version can be restored
     *
     * @param file File
     * @return True if this file version can be reverted
     */
    boolean isRevertable(Path file);

    /**
     * Prompt for MFA Authentication Code
     *
     * @param mfaSerial Serial number of device
     * @param callback  Prompt
     * @return MFA Code entered in prompt
     * @throws ConnectionCanceledException Prompt dismissed
     */
    Credentials getToken(String mfaSerial, PasswordCallback callback) throws ConnectionCanceledException;
}
