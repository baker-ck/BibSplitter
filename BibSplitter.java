// Author: C. K. Baker
// Source: ChatGPT 4.1
// Prompt 1: write a simple java program that reads in a .bib (plaintext file) containing multiple bib entries and then writes each entry to a separate .bib file using the keyword as file name. the syntax is @type{tag, title={}, ..., }
// Prompt 2: dont accept runtime args, assume file is input.bib and output must be 1.bib 2.bib etc. so that the last file indicates the number of entries total ifrom the input

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class BibSplitter{

    public static void main(String[] args) throws IOException {
        Path inputFile = Paths.get("input.bib");

        if (!Files.exists(inputFile)) {
            System.out.println("input.bib not found");
            return;
        }

        String content = Files.readString(inputFile);
        List<String> entries = splitBibEntries(content);

        int count = 0;
        for (String entry : entries) {
            count++;
            Path outFile = Paths.get(count + ".bib");
            Files.writeString(outFile, entry.trim() + System.lineSeparator());
        }

        System.out.println("Total entries written: " + count);
    }

    // Split input into individual BibTeX entries
    private static List<String> splitBibEntries(String text) {
        List<String> entries = new ArrayList<>();

        int depth = 0;
        int start = -1;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            if (c == '@' && depth == 0) {
                start = i;
            }

            if (c == '{') depth++;
            if (c == '}') depth--;

            if (depth == 0 && start != -1 && c == '}') {
                entries.add(text.substring(start, i + 1));
                start = -1;
            }
        }

        return entries;
    }
}
