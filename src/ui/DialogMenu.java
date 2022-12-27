package ui;

import helpers.Input;

public final class DialogMenu<T extends Enum<?>> {
    private T[] options;
    private String dialogPicture;

    public DialogMenu(T[] options) {
        this.options = options;
    }

    private int getMaxOptionLength(T[] options) {
        int maxLength = 0;
        for (T option : options)
            if (option.name().length() > maxLength)
                maxLength = option.name().length();
        return maxLength;
    }

    private String build() {
        int padd = 6;
        int totalRows = options.length + 4;
        int charsInLine = 2 + padd + 3 + getMaxOptionLength(options) + padd + 2;
        StringBuilder stringBuilder = new StringBuilder();
        for (int row = 0; row < totalRows; row++) {
            for (int i = 0; i < charsInLine; i++) {
                if (i <= 1 || i >= charsInLine - 2)
                    stringBuilder.append('|');
                else if (row == 0 || row == totalRows - 1)
                    stringBuilder.append('=');
                else if (i == 2 + padd && row > 1 && row < totalRows - 2) {
                    stringBuilder.append((row - 1) + ". " + options[row - 2]);
                    i += options[row - 2].name().length() + 2;
                } else
                    stringBuilder.append(' ');
            }
            stringBuilder.append(System.lineSeparator());
        }
        return stringBuilder.toString();
    }

    public T getOption() {
        return options[Input.getInt("Your option >> ", 1, options.length) - 1];
    }

    @Override
    public String toString() {
        if (dialogPicture == null)
            dialogPicture = build();
        return dialogPicture;
    }

}
