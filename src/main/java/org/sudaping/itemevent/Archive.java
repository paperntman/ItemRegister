package org.sudaping.itemevent;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Archive {

    private File file;
    private String temp;
    private static final List<Archive> waiting = new ArrayList<>();

    public static Archive load(Class<?> clazz) {
        if (Main.dataFolder == null){
            Archive e = new Archive(clazz.getSimpleName());
            waiting.add(e);
            return e;
        }else return new Archive(new File(Main.dataFolder + File.separator + "archive" + File.separator + clazz.getSimpleName() + ".json"));
    }

    public static void loadWaiting(){
        for (Archive archive : waiting) {
            archive.file = new File(Main.dataFolder + File.separator + "archive" + File.separator + archive.getTemp() + ".json");
        }
        new File(Main.dataFolder + File.separator + "archive").mkdirs();
    }

    public Archive(@NotNull File file) {
        this.file = file;
    }

    public Archive(String temp) {
        this.temp = temp;
    }

    public String read(){
        try (BufferedReader br = new BufferedReader(new FileReader(this.file))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                line = br.readLine();
                sb.append("\n");
            }
            return sb.toString();
        } catch (FileNotFoundException e){
            return "";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void write(String text){
        file.getParentFile().mkdirs();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(this.file))) {
            bw.write(text);
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    public boolean exists(){
        return this.file.exists();
    }

    public String getTemp() {
        return temp;
    }
}
