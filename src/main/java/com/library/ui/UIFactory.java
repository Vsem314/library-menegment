package com.library.ui;

import com.library.Library;
import java.util.Objects;

public class UIFactory {
    public enum UIType {
        CONSOLE,
        LANTERNA
    }

    public static BaseLibraryUI createUI(UIType type, Library library) {
        Objects.requireNonNull(library, "Library cannot be null");
        Objects.requireNonNull(type, "UIType cannot be null");

        switch (type) {
            case LANTERNA: return new LanternaLibraryUI(library);
            case CONSOLE: return new ConsoleLibraryUI(library);
            default:
                throw new IllegalArgumentException("UI type not supported: " + type);
        }
    }
}