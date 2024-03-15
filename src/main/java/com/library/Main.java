package com.library;

import com.library.ui.BaseLibraryUI;
import com.library.ui.UIFactory;
import com.library.ui.UIFactory.UIType;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            Library library = new Library();
            BaseLibraryUI ui = UIFactory.createUI(UIType.LANTERNA, library);
            ui.start();
        } catch (Exception e) {
            System.err.println("Critical error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}