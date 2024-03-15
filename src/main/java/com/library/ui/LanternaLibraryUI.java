package com.library.ui;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.library.Book;
import com.library.Library;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

public class LanternaLibraryUI extends BaseLibraryUI {
    private WindowBasedTextGUI gui;
    private Screen screen;

    public LanternaLibraryUI(Library library) {
        super(library);
    }

    @Override
    protected WindowBasedTextGUI createTextGUI() throws IOException {
        screen = new DefaultTerminalFactory().createScreen();
        screen.startScreen();
        return new MultiWindowTextGUI(screen);
    }

    @Override
    public void start() throws IOException {
        try {
            gui = createTextGUI();
            MainWindow mainWindow = new MainWindow(gui, library);
            gui.addWindowAndWait(mainWindow);
        } finally {
            shutdown();
        }
    }

    @Override
    public void shutdown() {
        try {
            if (screen != null) {
                screen.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing screen: " + e.getMessage());
        }
    }

    private static class MainWindow extends BasicWindow {
        public MainWindow(WindowBasedTextGUI gui, Library library) {
            super("Library Management");
            setHints(List.of(Window.Hint.CENTERED));

            Panel panel = new Panel(new GridLayout(2));

            panel.addComponent(new Button("Add Book", () ->
                    new BookFormWindow(gui, library, null).show()));
            panel.addComponent(new Button("List Books", () ->
                    new BookListWindow(gui, library).show()));
            panel.addComponent(new Button("Exit", this::close));

            setComponent(panel);
        }
    }

    private static class BookFormWindow extends BasicWindow {
        private final WindowBasedTextGUI gui;
        private static final Pattern ID_PATTERN = Pattern.compile("[A-Za-z0-9-]+");
        private static final Pattern YEAR_PATTERN = Pattern.compile("\\d+");
        private static final Pattern PRICE_PATTERN = Pattern.compile("\\d+\\.?\\d*");
        private final Library library;

        public BookFormWindow(WindowBasedTextGUI gui, Library library, Book existingBook) {
            super(existingBook == null ? "Add Book" : "Edit Book");
            this.gui = gui;
            this.library = library;
            setHints(List.of(Window.Hint.CENTERED));

            Panel panel = new Panel(new GridLayout(2));

            TextBox idField = new TextBox(existingBook != null ? existingBook.getId() : "")
                    .setValidationPattern(ID_PATTERN)
                    .setReadOnly(existingBook != null);

            TextBox titleField = new TextBox(existingBook != null ? existingBook.getTitle() : "");
            TextBox authorField = new TextBox(existingBook != null ? existingBook.getAuthor() : "");
            TextBox yearField = new TextBox(existingBook != null ? String.valueOf(existingBook.getYear()) : "")
                    .setValidationPattern(YEAR_PATTERN);
            TextBox priceField = new TextBox(existingBook != null ? String.format("%.2f", existingBook.getPrice()) : "")
                    .setValidationPattern(PRICE_PATTERN);
            TextBox genreField = new TextBox(existingBook != null ? existingBook.getGenre() : "");
            CheckBox availableCheckbox = new CheckBox().setChecked(existingBook == null || existingBook.isAvailable());

            panel.addComponent(new Label("ID:"));
            panel.addComponent(idField);
            panel.addComponent(new Label("Title:"));
            panel.addComponent(titleField);
            panel.addComponent(new Label("Author:"));
            panel.addComponent(authorField);
            panel.addComponent(new Label("Year:"));
            panel.addComponent(yearField);
            panel.addComponent(new Label("Price:"));
            panel.addComponent(priceField);
            panel.addComponent(new Label("Genre:"));
            panel.addComponent(genreField);
            panel.addComponent(new Label("Available:"));
            panel.addComponent(availableCheckbox);

            panel.addComponent(new Button("Save", () -> {
                try {
                    Book book = new Book(
                            idField.getText(),
                            titleField.getText(),
                            authorField.getText(),
                            Integer.parseInt(yearField.getText()),
                            Double.parseDouble(priceField.getText()),
                            genreField.getText()
                    );
                    book.setAvailable(availableCheckbox.isChecked());

                    if (existingBook == null) {
                        library.addBook(book);
                        MessageDialog.showMessageDialog(gui, "Success", "Book added successfully!", MessageDialogButton.OK);
                    } else {
                        library.updateBook(existingBook.getId(), book);
                        MessageDialog.showMessageDialog(gui, "Success", "Book updated successfully!", MessageDialogButton.OK);
                    }

                    // Закрываем текущее окно формы
                    close();

                    // Показываем обновленный список книг
                    new BookListWindow(gui, library).show();

                } catch (Exception e) {
                    MessageDialog.showMessageDialog(gui, "Error", e.getMessage(), MessageDialogButton.OK);
                }
            }));

            panel.addComponent(new Button("Cancel", this::close));
            setComponent(panel);
        }

        public void show() {
            gui.addWindowAndWait(this);
        }
    }

    private static class BookListWindow extends BasicWindow {
        private final WindowBasedTextGUI gui;

        public BookListWindow(WindowBasedTextGUI gui, Library library) {
            super("Book List");
            this.gui = gui;
            setHints(List.of(Window.Hint.CENTERED));

            Panel panel = new Panel(new LinearLayout(Direction.VERTICAL));
            List<Book> books = library.getAllBooks();

            if (books.isEmpty()) {
                panel.addComponent(new Label("No books in library"));
            } else {
                books.forEach(book ->
                        panel.addComponent(new Label(book.toString())));
            }

            panel.addComponent(new Button("Close", this::close));
            setComponent(panel);
        }

        public void show() {
            gui.addWindowAndWait(this);
        }
    }
}