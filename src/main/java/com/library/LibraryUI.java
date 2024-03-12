package com.library;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.screen.*;
import com.googlecode.lanterna.terminal.*;

import java.io.IOException;

public class LibraryUI {
    public static void start(Library library) throws IOException {
        Terminal terminal = new DefaultTerminalFactory().createTerminal();
        Screen screen = new TerminalScreen(terminal);
        screen.startScreen();

        WindowBasedTextGUI gui = new MultiWindowTextGUI(screen);
        BasicWindow window = new BasicWindow("Библиотека");

        Panel panel = new Panel().setLayoutManager(new GridLayout(2));

        // Добавляем компоненты
        panel.addComponent(new Label("ID книги:"));
        TextBox idField = new TextBox().addTo(panel);

        panel.addComponent(new Label("Название:"));
        TextBox titleField = new TextBox().addTo(panel);

        // Кнопки действий
        panel.addComponent(new Button("Добавить", () -> {
            try {
                library.addBook(idField.getText(), titleField.getText());
                MessageDialog.showMessageDialog(gui, "Успех", "Книга добавлена");
            } catch (Exception e) {
                MessageDialog.showMessageDialog(gui, "Ошибка", e.getMessage());
            }
        }));

        window.setComponent(panel);
        gui.addWindowAndWait(window);
        screen.stopScreen();
    }
}
