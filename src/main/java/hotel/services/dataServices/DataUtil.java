package hotel.services.dataServices;

import org.springframework.stereotype.Component;
import hotel.exceptions.FileNotFoundException;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DataUtil {
    public List<String> readAllLines(String filepath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            return reader.lines().collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeAllLine(File file, String line) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(line);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void checkFileExists(String filepath) {
        File file = new File(filepath);
        if (!file.exists()) {
            throw new FileNotFoundException(DataUtil.class.getName() + "No file by this path - " + filepath);
        }
    }
}
