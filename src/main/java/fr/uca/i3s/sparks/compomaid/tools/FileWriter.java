package fr.uca.i3s.sparks.compomaid.tools;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileWriter {

    public static void writeInFile(String filename, String str) {
        // Replace __PCT__ by %
        str = str.replace("__PCT__", "%");
        // Write code in the file
        File file = new File(filename);

        if (!file.getParentFile().getParentFile().getParentFile().exists()) {
            System.out.println("Create directory");
            file.getParentFile().getParentFile().getParentFile().mkdir();
        }
        if (!file.getParentFile().getParentFile().exists()) {
            System.out.println(file.getParentFile().getParentFile());
            System.out.println(file.getParentFile().getParentFile().exists());
            System.out.println("Create directory");
            file.getParentFile().getParentFile().mkdir();
        }
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdir();
        }

        try {
            file.createNewFile();
            Files.write(Paths.get(filename), str.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
