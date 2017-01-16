package me.samboycoding.gowguildtool.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import static java.nio.charset.Charset.defaultCharset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Optional;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import static javafx.scene.control.Alert.AlertType.NONE;
import javafx.scene.control.ButtonType;
import static javafx.scene.control.ButtonType.OK;
import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Sam
 */
public class Utils
{

    /**
     * Represents a new line character. Use
     * {@link net.whcuk.utils.Utils#newLine(int)} for multiple of these easily.
     */
    public static final String NEWLINE = System.lineSeparator();

    /**
     * Downloads a file from the Internet
     *
     * @param url the URL to download from.
     * @param localDes the local destination to download to.
     * @throws java.lang.Exception if something goes wrong
     */
    public static void downloadFile(String url, String localDes) throws Exception
    {
        URL website = new URL(url);
        try (ReadableByteChannel rbc = Channels.newChannel(website.openStream()); FileOutputStream fos = new FileOutputStream(localDes))
        {
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        } catch(Exception ex)
        {
            throw ex; //Report it upwards
        }
        if(!(new File(localDes).exists()))
        {
            throw new IOException("File \"" + localDes + "\" was not downloaded!");
        }
    }

    public static Optional<ButtonType> msgBox(AlertType type, String message, ButtonType... btns)
    {
        Alert alert = new Alert(type, message, btns);
        return alert.showAndWait();
    }

    public static void msgBoxThreadSafe(final AlertType type, final String message, final ButtonType... btns)
    {
        Platform.runLater(new Runnable()
        {
            @Override
            public void run()
            {
                Alert alert = new Alert(type, message, btns);
                alert.showAndWait();
            }

        });
    }

    public static void simpleMsgBox(String message)
    {
        Alert alert = new Alert(NONE, message, OK);
        alert.showAndWait();
    }

    public static boolean fileExists(Path file)
    {
        File f = file.toFile();
        return f.exists() && !f.isDirectory();
    }

    public static boolean dirExists(Path file)
    {
        File f = file.toFile();
        return f.exists() && f.isDirectory();
    }

    public static void createFile(Path file) throws IOException
    {
        File f = file.toFile();
        if (!f.exists())
        {
            f.createNewFile();
        }
    }
    
    public static void createFile(Path file, String initialtext) throws IOException
    {
        File f = file.toFile();
        if (!f.exists())
        {
            f.createNewFile();
        }
        FileUtils.writeStringToFile(f, initialtext, defaultCharset());
    }

    public static void createDir(Path file) throws IOException
    {
        File f = file.toFile();
        if (!f.exists())
        {
            f.mkdirs();
        }
    }

    public static String newLine(int num)
    {
        String res = "";
        for (int i = 1; i <= num; i++)
        {
            res += System.lineSeparator();
        }
        return res;
    }

    public static int fileLength(Path file) throws IOException
    {
        return Files.readAllLines(file).size();
    }

    public static void delDirIfExist(Path dir) throws IOException
    {
        if (dirExists(dir))
        {
            File f = dir.toFile();
            FileUtils.deleteDirectory(f);
        }
    }

    public static void delFileIfExist(Path file) throws IOException
    {
        if (fileExists(file))
        {
            File f = file.toFile();
            f.delete();
        }
    }

    public static JSONObject getJsonFrom(Path path) throws IOException, JSONException
    {
        ArrayList<String> jsonRawArr = new ArrayList<>(Files.readAllLines(path));
        String jsonRaw = "";
        jsonRaw = jsonRawArr.stream().map((String jsonEl) -> jsonEl).reduce(jsonRaw, String::concat);
        return new JSONObject(jsonRaw);
    }
}
