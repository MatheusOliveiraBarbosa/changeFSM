/**
 * Copyright (c) 2014-2017 by the respective copyright holders.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.io.console.internal.extension;

import java.util.Collections;
import java.util.List;

import org.eclipse.smarthome.core.items.Item;
import org.eclipse.smarthome.core.items.ItemNotFoundException;
import org.eclipse.smarthome.core.items.ItemNotUniqueException;
import org.eclipse.smarthome.core.items.ItemRegistry;
import org.eclipse.smarthome.io.console.Console;
import org.eclipse.smarthome.io.console.extensions.AbstractConsoleCommandExtension;

/**
 * Console command extension to show the current state of an item
 *
 * @author Kai Kreuzer - Initial contribution and API
 * @author Markus Rathgeb - Create DS for command extension
 * @author Dennis Nobel - Changed service references to be injected via DS
 * 
 */
public class StatusConsoleCommandExtension extends AbstractConsoleCommandExtension {

    private ItemRegistry itemRegistry;

    public StatusConsoleCommandExtension() {
        super("status", "Get the current status of an item.");
    }

    @Override
    public List<String> getUsages() {
        return Collections.singletonList(buildCommandUsage("<item>", "shows the current status of an item"));
    }

    @Override
    public void execute(String[] args, Console console) {

        if (args.length > 0) {
            String itemName = args[0];
            try {
                Item item = this.itemRegistry.getItemByPattern(itemName);
                console.println(item.getState().toString());
            } catch (ItemNotFoundException e) {
                console.println("Error: Item '" + itemName + "' does not exist.");
            } catch (ItemNotUniqueException e) {
                console.print("Error: Multiple items match this pattern: ");
                for (Item item : e.getMatchingItems()) {
                    console.print(item.getName() + " ");
                }
            }
        } else {
            printUsage(console);
        }
    }

    protected void setItemRegistry(ItemRegistry itemRegistry) {
        this.itemRegistry = itemRegistry;
    }

    protected void unsetItemRegistry(ItemRegistry itemRegistry) {
        this.itemRegistry = null;
    }

}
