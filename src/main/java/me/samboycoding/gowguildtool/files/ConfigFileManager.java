package me.samboycoding.gowguildtool.files;

import java.io.File;
import java.io.IOException;
import static java.nio.charset.Charset.defaultCharset;
import javafx.scene.control.Alert;
import static javafx.scene.control.ButtonType.OK;
import me.samboycoding.gowguildtool.utils.Utils;
import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class to manage the configuration file.
 *
 * @author Sam
 */
public class ConfigFileManager
{

    private File confFile;
    private JSONObject data;
    public static ConfigFileManager inst;

    private String defaultConfig = "{\n"
            + "    \"FileLocation\": null,\n"
            + "    \"ServerURL\": null,\n"
            + "    \"Requirements\": {\n"
            + "        \"Gold\": 0,\n"
            + "        \"Seals\": 0,\n"
            + "        \"Trophies\": 0\n"
            + "    }\n"
            + "}";

    public ConfigFileManager()
    {
        inst = this;

        confFile = new File("./GuildToolConfig.json");
        try
        {
            if (!confFile.exists())
            {
                confFile.createNewFile();
                FileUtils.writeStringToFile(confFile, defaultConfig, "UTF-8", false);
                data = new JSONObject(defaultConfig);
            } else
            {
                try
                {
                    data = new JSONObject(FileUtils.readFileToString(confFile, defaultCharset()));
                } catch (JSONException ex)
                {
                    confFile.delete();
                    confFile.createNewFile();
                    FileUtils.writeStringToFile(confFile, defaultConfig, "UTF-8", false);
                    data = new JSONObject(defaultConfig);
                }
            }
        } catch (IOException e)
        {
            Utils.msgBox(Alert.AlertType.ERROR, "Unable to create/load configuration file: " + e.toString(), OK);
        }
    }
    
    public JSONObject getData()
    {
        return data;
    }
    
    public void updateData(JSONObject data) throws IOException
    {
        this.data = data;
        FileUtils.writeStringToFile(confFile, this.data.toString(4), "UTF-8", false);
    }
}
