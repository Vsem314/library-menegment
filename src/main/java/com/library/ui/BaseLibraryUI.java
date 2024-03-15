package com.library.ui;

import com.library.Library;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import java.io.IOException;
import java.util.Objects;

public abstract class BaseLibraryUI {
    protected final Library library;

    public BaseLibraryUI(Library library) {
        this.library = Objects.requireNonNull(library, "Library cannot be null");
    }

    public abstract void start() throws IOException;
    public abstract void shutdown();

    protected WindowBasedTextGUI createTextGUI() throws IOException {
        throw new UnsupportedOperationException("Text GUI not supported in this implementation");
    }
}