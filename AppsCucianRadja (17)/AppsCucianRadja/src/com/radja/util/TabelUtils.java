package com.radja.util;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import javax.swing.border.Border;
import java.util.logging.Logger;
import java.util.logging.Level;

public class TabelUtils {

    private static final Logger LOGGER = Logger.getLogger(TabelUtils.class.getName());

    // Mengatur lebar kolom untuk tabel dengan indeks eksplisit
    public static void setColumnWidths(JTable table, int[] columnIndices, int[] widths) {
        if (table == null || table.getColumnModel() == null) {
            LOGGER.log(Level.WARNING, "Table or column model is null");
            return;
        }
        if (columnIndices.length != widths.length) {
            LOGGER.log(Level.WARNING, "Mismatch: columnIndices length and widths length are different");
            throw new IllegalArgumentException("The length of columnIndices and widths must be the same");
        }

        TableColumnModel columnModel = table.getColumnModel();
        for (int i = 0; i < columnIndices.length; i++) {
            int columnIndex = columnIndices[i];
            int width = widths[i];

            if (columnIndex >= 0 && columnIndex < columnModel.getColumnCount()) {
                TableColumn column = columnModel.getColumn(columnIndex);
                column.setPreferredWidth(width);
                column.setMaxWidth(width);
                column.setMinWidth(width);
                LOGGER.log(Level.FINE, "Set width for column index: " + columnIndex);
            } else {
                LOGGER.log(Level.WARNING, "Invalid column index: " + columnIndex);
            }
        }
    }

    // Mengatur lebar kolom untuk tabel secara berurutan
    public static void setColumnWidths(JTable table, int[] widths) {
        if (table == null || table.getColumnModel() == null || widths == null) {
            LOGGER.log(Level.WARNING, "Table, column model, or widths is null");
            return;
        }
        TableColumnModel columnModel = table.getColumnModel();
        int columnCount = columnModel.getColumnCount();
        for (int i = 0; i < widths.length && i < columnCount; i++) {
            TableColumn column = columnModel.getColumn(i);
            if (column != null) {
                column.setPreferredWidth(widths[i]);
                LOGGER.log(Level.FINE, "Set width for column: " + i);
            }
        }
        if (widths.length != columnCount) {
            LOGGER.log(Level.WARNING, "Mismatch: widths length and table columns count are different");
        }
    }

    // Mengatur alignment kolom di tabel
    public static void setColumnAlignment(JTable table, int[] indices, int alignment) {
        if (table == null || table.getColumnModel() == null) {
            LOGGER.log(Level.WARNING, "Table or column model is null");
            return;
        }
        TableColumnModel columnModel = table.getColumnModel();
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setHorizontalAlignment(alignment);

        for (int index : indices) {
            if (index >= 0 && index < columnModel.getColumnCount()) {
                columnModel.getColumn(index).setCellRenderer(cellRenderer);
                LOGGER.log(Level.FINE, "Set alignment for column: " + index);
            } else {
                LOGGER.log(Level.WARNING, "Invalid column index: " + index);
            }
        }
    }

    // Mengatur alignment kolom dengan array alignments
    public static void setColumnAlignment(JTable table, int[] columns, int[] alignments) {
        if (table == null || columns == null || alignments == null) {
            LOGGER.log(Level.WARNING, "Table, columns, or alignments is null");
            return;
        }
        if (columns.length != alignments.length) {
            LOGGER.log(Level.WARNING, "Mismatch: columns length and alignments length are different");
            return;
        }
        TableColumnModel columnModel = table.getColumnModel();
        for (int i = 0; i < columns.length; i++) {
            if (columns[i] >= 0 && columns[i] < columnModel.getColumnCount()) {
                TableColumn column = columnModel.getColumn(columns[i]);
                DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
                renderer.setHorizontalAlignment(alignments[i]);
                column.setCellRenderer(renderer);
                LOGGER.log(Level.FINE, "Set alignment for column: " + columns[i]);
            } else {
                LOGGER.log(Level.WARNING, "Invalid column index: " + columns[i]);
            }
        }
    }

    // Mengatur alignment header tabel
    public static void setHeaderAlignment(JTable table, int[] columnIndices, int[] alignments, int defaultAlignment) {
        if (table == null || table.getTableHeader() == null) {
            LOGGER.log(Level.WARNING, "Table or table header is null");
            return;
        }
        if (columnIndices.length != alignments.length) {
            LOGGER.log(Level.WARNING, "Mismatch: columnIndices length and alignments length are different");
            throw new IllegalArgumentException("The length of columnIndices and alignments must be the same");
        }

        JTableHeader header = table.getTableHeader();
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (component instanceof JLabel) {
                    JLabel label = (JLabel) component;

                    boolean found = false;
                    for (int i = 0; i < columnIndices.length; i++) {
                        if (column == columnIndices[i]) {
                            label.setHorizontalAlignment(alignments[i]);
                            found = true;
                            break;
                        }
                    }

                    if (!found) {
                        label.setHorizontalAlignment(defaultAlignment);
                    }

                    Border border = BorderFactory.createMatteBorder(0, 0, 1, 0, UIManager.getColor("Table.gridColor"));
                    label.setBorder(border);
                }
                return component;
            }
        };

        TableColumnModel columnModel = table.getColumnModel();
        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            TableColumn column = columnModel.getColumn(i);
            column.setHeaderRenderer(headerRenderer);
        }

        table.setShowGrid(false);
        LOGGER.log(Level.INFO, "Header alignment set for columns: " + columnModel.getColumnCount());
    }

    private static int getAlignment(int[] columns, int[] alignments, int columnIndex, int defaultAlignment) {
        for (int i = 0; i < columns.length; i++) {
            if (columns[i] == columnIndex) {
                return alignments[i];
            }
        }
        return defaultAlignment;
    }
}