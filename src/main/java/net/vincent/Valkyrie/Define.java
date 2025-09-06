package net.vincent.Valkyrie;

import net.vincent.Valkyrie.Exceptions.IllegalFormatException;

import java.io.BufferedWriter;
import java.io.IOException;

public class Define {

    public Define() {
    }

    private void validateLabelAndValue(String label, String value, boolean isFloat) throws IllegalFormatException {
        if (!label.matches("^[a-zA-Z0-9_-]+$")) {
            throw new IllegalFormatException("Invalid label: " + label +
                    ". Only letters, digits, '_' and '-' are allowed.");
        }
        if (isFloat) {
            if (!value.matches("^-?\\d+(\\.\\d+)?([eE]-?\\d+)?$")) {
                throw new IllegalFormatException("Invalid value: " + value +
                        ". Must be a floating-point number.");
            }
        } else {
            if (!value.matches("^-?\\d+$")) {
                throw new IllegalFormatException("Invalid value: " + value +
                        ". Must be an integer.");
            }
        }
    }

    private void writeSectionAndAlign(BufferedWriter out, String osName, int alignBytes) throws IOException {
        if (osName.contains("mac")) {
            out.write("\t.section\t__DATA,__data\n");
            out.write("\t.align\t" + (int)(Math.log(alignBytes) / Math.log(2)) + "\n");
        } else if (osName.contains("nix") || osName.contains("nux")) {
            out.write("\t.section\t.data\n");
            out.write("\t.align\t" + alignBytes + "\n");
        } else if (osName.contains("win")) {
            out.write("\t.data\n");
            out.write("\t.align\t" + alignBytes + "\n");
        } else {
            throw new UnsupportedOperationException("Unsupported OS: " + osName);
        }
    }

    public void defInt(BufferedWriter out, String[] tokens, String directive) throws IllegalFormatException {
        if (tokens.length < 3) {
            throw new IllegalFormatException("Usage: " + directive + " <label> <intValue>");
        }
        String label = tokens[1];
        String value = tokens[2];
        validateLabelAndValue(label, value, false);
        try {
            writeSectionAndAlign(out, System.getProperty("os.name").toLowerCase(), 4);
            out.write(label + ":\n\t.word\t" + value + "\n\n");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void defFloat(BufferedWriter out, String[] tokens, String directive) throws IllegalFormatException {
        if (tokens.length < 3) {
            throw new IllegalFormatException("Usage: " + directive + " <label> <floatValue>");
        }
        String label = tokens[1];
        String value = tokens[2];
        validateLabelAndValue(label, value, true);
        try {
            writeSectionAndAlign(out, System.getProperty("os.name").toLowerCase(), 4);
            out.write(label + ":\n\t.float\t" + value + "\n\n");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void defDouble(BufferedWriter out, String[] tokens, String directive) throws IllegalFormatException {
        if (tokens.length < 3) {
            throw new IllegalFormatException("Usage: " + directive + " <label> <doubleValue>");
        }
        String label = tokens[1];
        String value = tokens[2];
        validateLabelAndValue(label, value, true);
        try {
            writeSectionAndAlign(out, System.getProperty("os.name").toLowerCase(), 8);
            out.write(label + ":\n\t.double\t" + value + "\n\n");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void defChar(BufferedWriter out, String[] tokens, String directive) throws IllegalFormatException {
        if (tokens.length < 3) {
            throw new IllegalFormatException("Usage: " + directive + " <label> <char|'A'|65>");
        }
        String label = tokens[1];
        String value = tokens[2];
        if (!label.matches("^[a-zA-Z0-9_-]+$")) {
            throw new IllegalFormatException("Invalid label: " + label);
        }
        if (!(value.matches("^'.'$") || value.matches("^\\d+$"))) {
            throw new IllegalFormatException("Invalid char value: " + value);
        }
        try {
            writeSectionAndAlign(out, System.getProperty("os.name").toLowerCase(), 1);
            out.write(label + ":\n\t.byte\t" + value + "\n\n");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void defBool(BufferedWriter out, String[] tokens, String directive) throws IllegalFormatException {
        if (tokens.length < 3) {
            throw new IllegalFormatException("Usage: " + directive + " <label> <0|1>");
        }
        String label = tokens[1];
        String value = tokens[2];
        if (!label.matches("^[a-zA-Z0-9_-]+$")) {
            throw new IllegalFormatException("Invalid label: " + label);
        }
        if (!(value.equals("0") || value.equals("1"))) {
            throw new IllegalFormatException("Invalid bool value: " + value);
        }
        try {
            writeSectionAndAlign(out, System.getProperty("os.name").toLowerCase(), 1);
            out.write(label + ":\n\t.byte\t" + value + "\n\n");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}