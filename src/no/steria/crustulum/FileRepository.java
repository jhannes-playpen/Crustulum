package no.steria.crustulum;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public abstract class FileRepository<T> {

    private final File file;

    public FileRepository(File file) {
        this.file = file;
    }

    protected DateTimeFormatter dateTimeFormat = DateTimeFormat.forPattern("yyyy/MM/dd");

    protected void writeContents(Collection<T> items) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            for (T item : items) {
                writer.write(writeItem(item));
                writer.write("\n");
            }
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract String writeItem(T item);

    protected List<T> readItems() {
        ArrayList<T> result = new ArrayList<T>();
        if (!file.exists()) return result;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                result.add(readLine(line));
            }
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    protected abstract T readLine(String line);
}
