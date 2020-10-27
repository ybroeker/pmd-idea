package com.github.ybroeker.pmdidea;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;

import com.intellij.openapi.components.Service;


@Service
public final class LevelFilterModel {

    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    private final Set<Level> selected = EnumSet.allOf(Level.class);

    public boolean isVisible(final Level level) {
        return selected.contains(level);
    }

    public void setVisible(final Level level, final boolean visible) {
        final HashSet<Level> previousSelected = new HashSet<>(selected);
        if (visible) {
            selected.add(level);
        } else {
            selected.remove(level);
        }
        propertyChangeSupport.firePropertyChange("selected", previousSelected, selected);
    }

    public void addPropertyChangeListener(final PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(final PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    public Set<Level> getSelected() {
        return Collections.unmodifiableSet(selected);
    }
}
