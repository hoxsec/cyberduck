package ch.cyberduck.core.sds;

/*
 * Copyright (c) 2002-2020 iterate GmbH. All rights reserved.
 * https://cyberduck.io/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */

import ch.cyberduck.core.AlphanumericRandomStringService;
import ch.cyberduck.core.Credentials;
import ch.cyberduck.core.DisabledConnectionCallback;
import ch.cyberduck.core.DisabledLoginCallback;
import ch.cyberduck.core.Host;
import ch.cyberduck.core.Local;
import ch.cyberduck.core.LoginOptions;
import ch.cyberduck.core.Path;
import ch.cyberduck.core.PathAttributes;
import ch.cyberduck.core.exception.ConflictException;
import ch.cyberduck.core.features.Delete;
import ch.cyberduck.core.io.BandwidthThrottle;
import ch.cyberduck.core.io.DisabledStreamListener;
import ch.cyberduck.core.sds.triplecrypt.TripleCryptReadFeature;
import ch.cyberduck.core.transfer.Transfer;
import ch.cyberduck.core.transfer.TransferItem;
import ch.cyberduck.core.transfer.TransferStatus;
import ch.cyberduck.core.vault.VaultCredentials;
import ch.cyberduck.test.IntegrationTest;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.EnumSet;
import java.util.UUID;

import static org.junit.Assert.*;

@Category(IntegrationTest.class)
public class SDSDirectS3UploadFeatureTest extends AbstractS3DirectSDSTest {

    @Test
    public void testUploadZeroByteFile() throws Exception {
        final SDSNodeIdProvider nodeid = new SDSNodeIdProvider(session);
        final SDSDirectS3UploadFeature feature = new SDSDirectS3UploadFeature(session, nodeid, new SDSDelegatingWriteFeature(session, nodeid, new SDSDirectS3WriteFeature(session, nodeid)));
        final Path room = new SDSDirectoryFeature(session, nodeid).mkdir(
                new Path(new AlphanumericRandomStringService().random(), EnumSet.of(Path.Type.directory, Path.Type.volume)), new TransferStatus());
        final Path test = new Path(room, new AlphanumericRandomStringService().random(), EnumSet.of(Path.Type.file));
        final Local local = new Local(System.getProperty("java.io.tmpdir"), UUID.randomUUID().toString());
        final byte[] random = RandomUtils.nextBytes(0);
        final OutputStream out = local.getOutputStream(false);
        IOUtils.write(random, out);
        out.close();
        final TransferStatus status = new TransferStatus();
        status.setLength(random.length);
        feature.upload(test, local, new BandwidthThrottle(BandwidthThrottle.UNLIMITED),
                new DisabledStreamListener(), status, new DisabledLoginCallback());
        assertTrue(new SDSFindFeature(session, nodeid).find(test));
        final PathAttributes attributes = new SDSAttributesFinderFeature(session, nodeid).find(test);
        assertEquals(random.length, attributes.getSize());
        try {
            feature.upload(test, local, new BandwidthThrottle(BandwidthThrottle.UNLIMITED),
                    new DisabledStreamListener(), status, new DisabledLoginCallback());
            fail();
        }
        catch(ConflictException e) {
        }
        new SDSDeleteFeature(session, nodeid).delete(Collections.singletonList(room), new DisabledLoginCallback(), new Delete.DisabledCallback());
        local.delete();
    }

    @Test
    public void testUploadBelowMultipartSize() throws Exception {
        final SDSNodeIdProvider nodeid = new SDSNodeIdProvider(session);
        final SDSDirectS3UploadFeature feature = new SDSDirectS3UploadFeature(session, nodeid, new SDSDelegatingWriteFeature(session, nodeid, new SDSDirectS3WriteFeature(session, nodeid)));
        final Path room = new SDSDirectoryFeature(session, nodeid).mkdir(
                new Path(new AlphanumericRandomStringService().random(), EnumSet.of(Path.Type.directory, Path.Type.volume)), new TransferStatus());
        final Path test = new Path(room, new AlphanumericRandomStringService().random(), EnumSet.of(Path.Type.file));
        final Local local = new Local(System.getProperty("java.io.tmpdir"), UUID.randomUUID().toString());
        final byte[] random = RandomUtils.nextBytes(578);
        final OutputStream out = local.getOutputStream(false);
        IOUtils.write(random, out);
        out.close();
        final TransferStatus status = new TransferStatus();
        status.setLength(random.length);
        feature.upload(test, local, new BandwidthThrottle(BandwidthThrottle.UNLIMITED),
                new DisabledStreamListener(), status, new DisabledLoginCallback());
        assertTrue(new SDSFindFeature(session, nodeid).find(test));
        final PathAttributes attributes = new SDSAttributesFinderFeature(session, nodeid).find(test);
        assertEquals(random.length, attributes.getSize());
        new SDSDeleteFeature(session, nodeid).delete(Collections.singletonList(room), new DisabledLoginCallback(), new Delete.DisabledCallback());
        local.delete();
    }

    @Test
    public void testUploadExactMultipartSize() throws Exception {
        final SDSNodeIdProvider nodeid = new SDSNodeIdProvider(session);
        final SDSDirectS3UploadFeature feature = new SDSDirectS3UploadFeature(session, nodeid, new SDSDelegatingWriteFeature(session, nodeid, new SDSDirectS3WriteFeature(session, nodeid)));
        final Path room = new SDSDirectoryFeature(session, nodeid).mkdir(
                new Path(new AlphanumericRandomStringService().random(), EnumSet.of(Path.Type.directory, Path.Type.volume)), new TransferStatus());
        final Path test = new Path(room, new AlphanumericRandomStringService().random(), EnumSet.of(Path.Type.file));
        final Local local = new Local(System.getProperty("java.io.tmpdir"), UUID.randomUUID().toString());
        final byte[] random = RandomUtils.nextBytes(10 * 1024 * 1024);
        final OutputStream out = local.getOutputStream(false);
        IOUtils.write(random, out);
        out.close();
        final TransferStatus status = new TransferStatus();
        status.setLength(random.length);
        feature.upload(test, local, new BandwidthThrottle(BandwidthThrottle.UNLIMITED),
                new DisabledStreamListener(), status, new DisabledLoginCallback());
        assertTrue(new SDSFindFeature(session, nodeid).find(test));
        final PathAttributes attributes = new SDSAttributesFinderFeature(session, nodeid).find(test);
        assertEquals(random.length, attributes.getSize());
        new SDSDeleteFeature(session, nodeid).delete(Collections.singletonList(room), new DisabledLoginCallback(), new Delete.DisabledCallback());
        local.delete();
    }

    @Test
    public void testUploadMultipleParts() throws Exception {
        final SDSNodeIdProvider nodeid = new SDSNodeIdProvider(session);
        final SDSDirectS3UploadFeature feature = new SDSDirectS3UploadFeature(session, nodeid, new SDSDelegatingWriteFeature(session, nodeid, new SDSDirectS3WriteFeature(session, nodeid)));
        final Path room = new SDSDirectoryFeature(session, nodeid).mkdir(
                new Path(new AlphanumericRandomStringService().random(), EnumSet.of(Path.Type.directory, Path.Type.volume)), new TransferStatus());
        final Path test = new Path(room, new AlphanumericRandomStringService().random(), EnumSet.of(Path.Type.file));
        final Local local = new Local(System.getProperty("java.io.tmpdir"), UUID.randomUUID().toString());
        final byte[] random = RandomUtils.nextBytes(21 * 1024 * 1024);
        final OutputStream out = local.getOutputStream(false);
        IOUtils.write(random, out);
        out.close();
        final TransferStatus status = new TransferStatus();
        status.setLength(random.length);
        feature.upload(test, local, new BandwidthThrottle(BandwidthThrottle.UNLIMITED),
                new DisabledStreamListener(), status, new DisabledLoginCallback());
        assertTrue(new SDSFindFeature(session, nodeid).find(test));
        final PathAttributes attributes = new SDSAttributesFinderFeature(session, nodeid).find(test);
        assertEquals(random.length, attributes.getSize());
        new SDSDeleteFeature(session, nodeid).delete(Collections.singletonList(room), new DisabledLoginCallback(), new Delete.DisabledCallback());
        local.delete();
    }

    @Test
    public void testTripleCryptUploadBelowMultipartSize() throws Exception {
        final SDSNodeIdProvider nodeid = new SDSNodeIdProvider(session);
        final SDSDirectS3UploadFeature feature = new SDSDirectS3UploadFeature(session, nodeid, new SDSDirectS3WriteFeature(session, nodeid));
        final Path room = new SDSDirectoryFeature(session, nodeid).createRoom(
                new Path(new AlphanumericRandomStringService().random(), EnumSet.of(Path.Type.directory, Path.Type.volume)), true);
        final Path test = new Path(room, new AlphanumericRandomStringService().random(), EnumSet.of(Path.Type.file));
        final Local local = new Local(System.getProperty("java.io.tmpdir"), UUID.randomUUID().toString());
        final byte[] random = RandomUtils.nextBytes(578);
        final OutputStream out = local.getOutputStream(false);
        IOUtils.write(random, out);
        out.close();
        final TransferStatus status = new TransferStatus();
        status.setFilekey(nodeid.getFileKey());
        status.setLength(random.length);
        final SDSEncryptionBulkFeature bulk = new SDSEncryptionBulkFeature(session, nodeid);
        bulk.pre(Transfer.Type.upload, Collections.singletonMap(new TransferItem(test, local), status), new DisabledConnectionCallback());
        feature.upload(test, local, new BandwidthThrottle(BandwidthThrottle.UNLIMITED),
                new DisabledStreamListener(), status, new DisabledLoginCallback());
        assertTrue(new SDSFindFeature(session, nodeid).find(test));
        final PathAttributes attributes = new SDSAttributesFinderFeature(session, nodeid).find(test);
        assertEquals(random.length, attributes.getSize());
        final byte[] compare = new byte[random.length];
        final InputStream stream = new TripleCryptReadFeature(session, nodeid, new SDSReadFeature(session, nodeid)).read(test, new TransferStatus(), new DisabledConnectionCallback() {
            @Override
            public Credentials prompt(final Host bookmark, final String title, final String reason, final LoginOptions options) {
                return new VaultCredentials("eth[oh8uv4Eesij");
            }
        });
        IOUtils.readFully(stream, compare);
        stream.close();
        assertArrayEquals(random, compare);
        new SDSDeleteFeature(session, nodeid).delete(Collections.singletonList(test), new DisabledLoginCallback(), new Delete.DisabledCallback());
        local.delete();
    }

    @Test
    public void testTripleCryptUploadExactMultipartSize() throws Exception {
        final SDSNodeIdProvider nodeid = new SDSNodeIdProvider(session);
        final SDSDirectS3UploadFeature feature = new SDSDirectS3UploadFeature(session, nodeid, new SDSDirectS3WriteFeature(session, nodeid));
        final Path room = new SDSDirectoryFeature(session, nodeid).createRoom(
                new Path(new AlphanumericRandomStringService().random(), EnumSet.of(Path.Type.directory, Path.Type.volume)), true);
        final Path test = new Path(room, new AlphanumericRandomStringService().random(), EnumSet.of(Path.Type.file));
        final Local local = new Local(System.getProperty("java.io.tmpdir"), UUID.randomUUID().toString());
        final byte[] random = RandomUtils.nextBytes(10 * 1024 * 1024);
        final OutputStream out = local.getOutputStream(false);
        IOUtils.write(random, out);
        out.close();
        final TransferStatus status = new TransferStatus();
        status.setFilekey(nodeid.getFileKey());
        status.setLength(random.length);
        final SDSEncryptionBulkFeature bulk = new SDSEncryptionBulkFeature(session, nodeid);
        bulk.pre(Transfer.Type.upload, Collections.singletonMap(new TransferItem(test, local), status), new DisabledConnectionCallback());
        feature.upload(test, local, new BandwidthThrottle(BandwidthThrottle.UNLIMITED),
                new DisabledStreamListener(), status, new DisabledLoginCallback());
        assertTrue(new SDSFindFeature(session, nodeid).find(test));
        final PathAttributes attributes = new SDSAttributesFinderFeature(session, nodeid).find(test);
        assertEquals(random.length, attributes.getSize());
        final byte[] compare = new byte[random.length];
        final InputStream stream = new TripleCryptReadFeature(session, nodeid, new SDSReadFeature(session, nodeid)).read(test, new TransferStatus(), new DisabledConnectionCallback() {
            @Override
            public Credentials prompt(final Host bookmark, final String title, final String reason, final LoginOptions options) {
                return new VaultCredentials("eth[oh8uv4Eesij");
            }
        });
        IOUtils.readFully(stream, compare);
        stream.close();
        assertArrayEquals(random, compare);
        new SDSDeleteFeature(session, nodeid).delete(Collections.singletonList(test), new DisabledLoginCallback(), new Delete.DisabledCallback());
        local.delete();
    }

    @Test
    public void testTripleCryptUploadMultipleParts() throws Exception {
        final SDSNodeIdProvider nodeid = new SDSNodeIdProvider(session);
        final SDSDirectS3UploadFeature feature = new SDSDirectS3UploadFeature(session, nodeid, new SDSDirectS3WriteFeature(session, nodeid));
        final Path room = new SDSDirectoryFeature(session, nodeid).createRoom(
                new Path(new AlphanumericRandomStringService().random(), EnumSet.of(Path.Type.directory, Path.Type.volume)), true);
        final Path test = new Path(room, new AlphanumericRandomStringService().random(), EnumSet.of(Path.Type.file));
        final Local local = new Local(System.getProperty("java.io.tmpdir"), UUID.randomUUID().toString());
        final byte[] random = RandomUtils.nextBytes(21 * 1024 * 1024);
        final OutputStream out = local.getOutputStream(false);
        IOUtils.write(random, out);
        out.close();
        final TransferStatus status = new TransferStatus();
        status.setFilekey(nodeid.getFileKey());
        status.setLength(random.length);
        final SDSEncryptionBulkFeature bulk = new SDSEncryptionBulkFeature(session, nodeid);
        bulk.pre(Transfer.Type.upload, Collections.singletonMap(new TransferItem(test, local), status), new DisabledConnectionCallback());
        feature.upload(test, local, new BandwidthThrottle(BandwidthThrottle.UNLIMITED),
                new DisabledStreamListener(), status, new DisabledLoginCallback());
        assertTrue(new SDSFindFeature(session, nodeid).find(test));
        final PathAttributes attributes = new SDSAttributesFinderFeature(session, nodeid).find(test);
        assertEquals(random.length, attributes.getSize());
        final byte[] compare = new byte[random.length];
        final InputStream stream = new TripleCryptReadFeature(session, nodeid, new SDSReadFeature(session, nodeid)).read(test, new TransferStatus(), new DisabledConnectionCallback() {
            @Override
            public Credentials prompt(final Host bookmark, final String title, final String reason, final LoginOptions options) {
                return new VaultCredentials("eth[oh8uv4Eesij");
            }
        });
        IOUtils.readFully(stream, compare);
        stream.close();
        assertArrayEquals(random, compare);
        new SDSDeleteFeature(session, nodeid).delete(Collections.singletonList(test), new DisabledLoginCallback(), new Delete.DisabledCallback());
        local.delete();
    }
}
