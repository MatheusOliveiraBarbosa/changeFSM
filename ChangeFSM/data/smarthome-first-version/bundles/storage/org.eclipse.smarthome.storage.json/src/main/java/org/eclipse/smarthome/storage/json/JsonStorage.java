/**
 * Copyright (c) 2014-2016 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.storage.json;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.smarthome.core.storage.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.LinkedTreeMap;

/**
 * The JsonStorage is concrete implementation of the {@link Storage} interface.
 * It stores the key-value pairs in files. This Storage serializes and deserializes
 * the given values using JSON (generated by {@code Gson}).
 * A deferred write mechanism of WRITE_DELAY milliseconds is used to improve performance.
 * The service keeps backups in a /backup folder, and maintains a maximum of MAX_FILES
 * at any time
 *
 * @author Chris Jackson - Initial Contribution
 */
public class JsonStorage<T> implements Storage<T> {

    private final Logger logger = LoggerFactory.getLogger(JsonStorage.class);

    private final int maxBackupFiles;
    private final int writeDelay;
    private final int maxDeferredPeriod;

    private final String CLASS = "class";
    private final String VALUE = "value";
    private final String BACKUP_EXTENSION = "backup";
    private final String SEPARATOR = "--";

    private Timer commitTimer = null;
    private TimerTask commitTimerTask = null;

    private long deferredSince = 0;

    private File file;
    private ClassLoader classLoader;
    private final Map<String, LinkedTreeMap<String, Object>> map = new ConcurrentHashMap<String, LinkedTreeMap<String, Object>>();

    private transient Gson mapper;

    public JsonStorage(File file, ClassLoader classLoader, int maxBackupFiles, int writeDelay, int maxDeferredPeriod) {
        this.file = file;
        this.classLoader = classLoader;
        this.maxBackupFiles = maxBackupFiles;
        this.writeDelay = writeDelay;
        this.maxDeferredPeriod = maxDeferredPeriod;

        this.mapper = new GsonBuilder().registerTypeAdapterFactory(new PropertiesTypeAdapterFactory())
                .setPrettyPrinting().create();
        commitTimer = new Timer();

        if (file.exists()) {
            Map<String, LinkedTreeMap<String, Object>> inputMap;

            // Read the file
            inputMap = readDatabase(file);

            // If there was an error reading the file, then try one of the backup files
            if (inputMap == null) {
                logger.info("Json storage file at '{}' was not read.", file.getAbsolutePath());
                for (int cnt = 1; cnt <= maxBackupFiles; cnt++) {
                    File backupFile = getBackupFile(cnt);
                    if (backupFile == null) {
                        break;
                    }
                    inputMap = readDatabase(backupFile);
                    if (inputMap != null) {
                        logger.info("Json storage file at '{}' is used (backup {}).", backupFile.getAbsolutePath(),
                                cnt);
                        break;
                    }
                }
            }

            // If we've read data from a file, then add it to the map
            if (inputMap != null) {
                map.putAll(inputMap);
            }
        }

        logger.debug("Opened Json storage file at '{}'.", file.getAbsolutePath());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T put(String key, T value) {
        LinkedTreeMap<String, Object> val = new LinkedTreeMap<String, Object>();
        val.put(CLASS, value.getClass().getName());
        val.put(VALUE, value);

        LinkedTreeMap<String, Object> previousValue = map.get(key);

        map.put(key, val);
        deferredCommit();

        if (previousValue == null) {
            return null;
        }

        return deserialize(previousValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T remove(String key) {
        LinkedTreeMap<String, Object> removedElement = map.remove(key);
        deferredCommit();
        return deserialize(removedElement);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T get(String key) {
        LinkedTreeMap<String, Object> value = map.get(key);
        if (value == null) {
            return null;
        }
        return deserialize(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<String> getKeys() {
        return map.keySet();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<T> getValues() {
        Collection<T> values = new ArrayList<T>();
        for (String key : getKeys()) {
            values.add(get(key));
        }
        return values;
    }

    /**
     * Deserializes and instantiates an object of type {@code T} out of the
     * given JSON String. A special classloader (other than the one of the
     * Json bundle) is used in order to load the classes in the context of
     * the calling bundle.
     */
    @SuppressWarnings("unchecked")
    private T deserialize(LinkedTreeMap<String, Object> jsonValue) {
        if (jsonValue == null) {
            // nothing to deserialize
            return null;
        }

        T value = null;
        try {
            // load required class within the given bundle context
            Class<T> loadedValueType = null;
            if (classLoader == null) {
                loadedValueType = (Class<T>) Class.forName((String) jsonValue.get(CLASS));
            } else {
                loadedValueType = (Class<T>) classLoader.loadClass((String) jsonValue.get(CLASS));
            }

            String jsonString = mapper.toJson(jsonValue.get("value"));
            value = mapper.fromJson(jsonString, loadedValueType);
            logger.trace("deserialized value '{}' from Json", value);
        } catch (Exception e) {
            logger.error("Couldn't deserialize value '{}'. Root cause is: {}", jsonValue, e.getMessage());
        }

        return value;
    }

    private Map<String, LinkedTreeMap<String, Object>> readDatabase(File inputFile) {
        try {
            final Map<String, LinkedTreeMap<String, Object>> inputMap = new ConcurrentHashMap<String, LinkedTreeMap<String, Object>>();

            FileReader reader = new FileReader(inputFile);
            HashMap<String, LinkedTreeMap<String, Object>> type = new HashMap<String, LinkedTreeMap<String, Object>>();
            HashMap<String, LinkedTreeMap<String, Object>> loadedMap = mapper.fromJson(reader, type.getClass());

            if (loadedMap != null && loadedMap.size() != 0) {
                map.putAll(loadedMap);
            }

            return inputMap;
        } catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
            logger.error("Error reading JsonDB from {}. Cause {}.", inputFile.getPath(), e.getMessage());
            return null;
        }
    }

    private File getBackupFile(int age) {
        // Delete old backups
        List<Long> fileTimes = new ArrayList<Long>();
        File folder = new File(file.getParent() + File.separator + BACKUP_EXTENSION);
        File[] files = folder.listFiles();

        // Get an array of file times from the filename
        int count = files.length;
        for (int i = 0; i < count; i++) {
            if (files[i].isFile()) {
                String[] parts = files[i].getName().split(SEPARATOR);
                if (parts.length != 2 || !parts[1].equals(file.getName())) {
                    continue;
                }
                long time = Long.parseLong(parts[0]);
                fileTimes.add(time);
            }
        }

        // Sort
        Collections.sort(fileTimes);
        if (fileTimes.size() < age) {
            return null;
        }

        return new File(file.getParent() + File.separator + BACKUP_EXTENSION,
                fileTimes.get(fileTimes.size() - age) + SEPARATOR + file.getName());
    }

    /**
     * Write out any outstanding data
     */
    public void commitDatabase() {
        String s = mapper.toJson(map);

        synchronized (map) {
            // Rename the file for backup
            File rename = new File(file.getParent() + File.separator + BACKUP_EXTENSION,
                    System.currentTimeMillis() + SEPARATOR + file.getName());
            file.renameTo(rename);

            try (FileOutputStream outputStream = new FileOutputStream(file, false);) {
                outputStream.write(s.getBytes());
                outputStream.close();
            } catch (Exception e) {
                logger.error("Error writing JsonDB to {}. Cause {}.", file.getPath(), e.getMessage());
            }

            deferredSince = 0;
        }
    }

    private class CommitTimerTask extends TimerTask {
        @Override
        public void run() {
            // Save the database
            commitDatabase();

            // Delete old backups
            List<Long> fileTimes = new ArrayList<Long>();
            File folder = new File(file.getParent() + File.separator + BACKUP_EXTENSION);
            File[] files = folder.listFiles();

            // Get an array of file times from the filename
            int count = files.length;
            for (int i = 0; i < count; i++) {
                if (files[i].isFile()) {
                    String[] parts = files[i].getName().split(SEPARATOR);
                    if (parts.length != 2 || !parts[1].equals(file.getName())) {
                        continue;
                    }
                    long time = Long.parseLong(parts[0]);
                    fileTimes.add(time);
                }
            }

            // Sort, and delete the oldest
            Collections.sort(fileTimes);
            if (fileTimes.size() > maxBackupFiles) {
                for (int counter = 0; counter < fileTimes.size() - maxBackupFiles; counter++) {
                    File deleter = new File(file.getParent() + File.separator + BACKUP_EXTENSION,
                            fileTimes.get(counter) + SEPARATOR + file.getName());
                    deleter.delete();
                }
            }
        }
    }

    public synchronized void deferredCommit() {
        // Handle a maximum time for deferring the commit.
        // This stops a pathological loop preventing saving
        if (deferredSince != 0 && deferredSince < System.nanoTime() - maxDeferredPeriod) {
            commitDatabase();
        }
        if (deferredSince == 0) {
            deferredSince = System.nanoTime();
        }

        // Stop any existing timer
        if (commitTimerTask != null) {
            commitTimerTask.cancel();
            commitTimerTask = null;
        }

        // Create the timer task
        commitTimerTask = new CommitTimerTask();

        // Start the timer
        commitTimer.schedule(commitTimerTask, writeDelay);
    }

}
