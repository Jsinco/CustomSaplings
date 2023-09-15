package me.jsinco.customsaplings;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;

public class FileManager {

    File[] schematics;
    private final CustomSaplings plugin;

    public FileManager(CustomSaplings plugin) {
        this.plugin = plugin;
    }

    public void loadSchematicFiles() {
        File schematicsFolder = new File(plugin.getDataFolder(), "Schematics");
        if (!schematicsFolder.exists()) {
            schematicsFolder.mkdir();
        }

        schematics = schematicsFolder.listFiles();
        if (schematics != null && schematics.length == 0) {
            defaultSchematics();
        }
    }

    public void defaultSchematics() {
        String[] defaultSchematics = {"apexplas.schem", "khan_floating_towers.schem"};
        for (String fileName : defaultSchematics) {
            File file = new File(plugin.getDataFolder(), "Schematics" + File.separator + fileName);
            try {
                if (!file.exists()) {
                    file.createNewFile();
                    InputStream inputStream = plugin.getResource("Schematics" + File.separator + fileName);
                            OutputStream outputStream = Files.newOutputStream(file.toPath());
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                    inputStream.close();
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

}
