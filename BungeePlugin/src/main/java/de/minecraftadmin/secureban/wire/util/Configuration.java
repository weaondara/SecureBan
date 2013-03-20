package de.minecraftadmin.secureban.wire.util;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Dustin
 * Date: 20.03.13
 * Time: 21:07
 * To change this template use File | Settings | File Templates.
 */
public class Configuration {

    private Map config;
    private final File configFile;
    private Yaml yml;

    public Configuration(File pluginFolder) throws IOException {
        configFile = new File(pluginFolder, "config.yml");
        if (configFile.exists()) {
            load(configFile);
        } else {
            if (!pluginFolder.exists()) pluginFolder.mkdir();
            configFile.createNewFile();
            InputStream defaultCfg = this.getClass().getResourceAsStream("/config.yml");
            FileWriter fw = new FileWriter(configFile);
            BufferedReader reader = new BufferedReader(new InputStreamReader(defaultCfg));
            char[] buf = new char[1024];
            int numRead = 0;
            String cfg = "";
            while ((numRead = reader.read(buf)) != -1) {
                String readData = String.valueOf(buf, 0, numRead);
                cfg += readData;
                buf = new char[1024];
            }
            fw.write(cfg);
            fw.flush();
            fw.close();
            load(configFile);
        }
    }

    private void load(File configFile) throws FileNotFoundException {
        DumperOptions options = new DumperOptions();

        options.setLineBreak(DumperOptions.LineBreak.getPlatformLineBreak());
        options.setAllowUnicode(true);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        yml = new Yaml(options);
        this.config = (Map) yml.load(new FileInputStream(configFile));
    }

    private void save() {
        if (yml == null) return;
        try {
            FileWriter fw = new FileWriter(configFile);
            yml.dump(config, fw);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    private <T> T get(String path, Class<T> clazz) {
        return get(path, clazz, config);
    }

    private <T> T get(String path, Class<T> clazz, Map config) {
        int index = path.indexOf('.');
        if (index == -1) {
            Object val = config.get(path);
            if (val == null) {
                return null;
            }
            return (T) val;
        } else {
            String first = path.substring(0, index);
            String second = path.substring(index + 1, path.length());
            Map sub = (Map) config.get(first);
            if (sub == null) {
                sub = new LinkedHashMap();
                config.put(first, sub);
            }
            return get(second, clazz, sub);
        }
    }

    public int getInteger(String node) {
        return get(node, Integer.class);
    }

    public String getString(String node) {
        return get(node, String.class);
    }

    public boolean getBoolean(String node) {
        Boolean bool = get(node, Boolean.class);
        if (bool == null) return false;
        return bool;
    }
}
