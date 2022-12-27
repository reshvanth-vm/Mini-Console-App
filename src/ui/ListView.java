package ui;

import java.util.List;
import java.util.function.Function;

import helpers.Input;

public class ListView<T> {
    private List<T> items;

    public ListView(List<T> items) {
        this.items = items;
    }

    public int noOfDigits(int num) {
        int digits = 0;
        for (int size = items.size() + 1; size > 0; size /= 10)
            digits++;
        return digits;
    }

    public void listItems() {
        listItems((item) -> item.toString().split(System.lineSeparator()));
    }

    public void listItems(Function<T, String[]> function) {
        int i = 1;
        String firstLinePrefix = "%" + noOfDigits(items.size()) + "d. %s";
        String linePrefix = firstLinePrefix.replaceFirst("d.", "c  ");
        for (T item : items) {
            int j = 0;
            for (String line : function.apply(item))
                System.out.println(String.format(j++ == 0 ? firstLinePrefix : linePrefix, i, line));
            i++;
            System.out.println();
        }
    }

    public T selectItemByUser(String message) {
        if (items.isEmpty())
            return null;
        int itemNo = Input.getInt(message, 0, items.size());
        return itemNo == 0 ? null : items.get(itemNo - 1);
    }

}
