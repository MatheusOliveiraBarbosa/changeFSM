/**
 * Copyright (c) 2014-2017 by the respective copyright holders.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.config.core;

/**
 * The {@link ParameterOption} specifies one option of a static selection list.
 * <p>
 * A {@link ConfigDescriptionParameter} instance can contain a list of {@link ParameterOption}s to define a static
 * selection list for the parameter value.
 *
 * @author Alex Tugarev - Initial Contribution
 *
 */
public class ParameterOption {

    private String label;
    private String value;

    /**
     * Default constructor for deserialization e.g. by Gson.
     */
    protected ParameterOption() {
    }

    public ParameterOption(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " [value=\"" + value + "\", label=\"" + label + "\"]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((label == null) ? 0 : label.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ParameterOption other = (ParameterOption) obj;
        if (label == null) {
            if (other.label != null) {
                return false;
            }
        } else if (!label.equals(other.label)) {
            return false;
        }
        if (value == null) {
            if (other.value != null) {
                return false;
            }
        } else if (!value.equals(other.value)) {
            return false;
        }
        return true;
    }

}
