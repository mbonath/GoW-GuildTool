package me.samboycoding.gowguildtool;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import static javafx.scene.control.ButtonType.OK;
import javafx.scene.control.TableView;
import me.samboycoding.gowguildtool.utils.Utils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class to handle network requests.
 *
 * @author Sam
 */
public class NetHandler
{

    public static String loadUserData(String uname, String pwd) throws UnsupportedEncodingException, IOException
    {
        JSONObject payload = new JSONObject();

        payload.put("username", uname);
        payload.put("password", pwd);
        payload.put("functionName", "login_user");
        payload.put("clientCallTime", System.currentTimeMillis());
        payload.put("ClientVersionNumber", "2.2.007");

        String postData = payload.toString(4);

        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpPost post_get_userdata = new HttpPost("http://gemsofwar.us-east-1.elasticbeanstalk.com/call_function");
        post_get_userdata.setEntity(new StringEntity(postData));
        post_get_userdata.setHeader("Content-Type", "application/json");

        JSONObject result;

        try (CloseableHttpResponse response_get_userdata = httpclient.execute(post_get_userdata))
        {
            HttpEntity entity2 = response_get_userdata.getEntity();

            StringWriter writer = new StringWriter();
            IOUtils.copy(entity2.getContent(), writer, "UTF-8");
            String theString = writer.toString();

            File save = new File("gameData.json");

            if (save.exists())
            {
                save.delete();
            }

            save.createNewFile();

            result = new JSONObject(theString);
            FileUtils.writeStringToFile(save, result.toString(4));
            EntityUtils.consume(entity2);

            return result.toString(4);
        } catch (JSONException e)
        {
            return null;
        }
    }

    public static String loadGuildData(JSONObject userdata) throws UnsupportedEncodingException, IOException
    {
        JSONObject payload = new JSONObject();

        payload.put("username", userdata.getJSONObject("result").getJSONObject("pCredentials").getString("username"));
        payload.put("password", userdata.getJSONObject("result").getJSONObject("pCredentials").getString("password"));
        payload.put("functionName", "get_guild_data");
        payload.put("clientCallTime", System.currentTimeMillis());
        payload.put("serverToken", userdata.getJSONObject("result").getString("ServerToken"));
        payload.put("ClientVersionNumber", "2.2.007");

        String postData = payload.toString(4);

        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpPost post_get_guilddata = new HttpPost("http://gemsofwar.us-east-1.elasticbeanstalk.com/call_function");
        post_get_guilddata.setEntity(new StringEntity(postData));
        post_get_guilddata.setHeader("Content-Type", "application/json");

        JSONObject result;

        try (CloseableHttpResponse response_get_guilddata = httpclient.execute(post_get_guilddata))
        {
            HttpEntity entity2 = response_get_guilddata.getEntity();

            StringWriter writer = new StringWriter();
            IOUtils.copy(entity2.getContent(), writer, "UTF-8");
            String theString = writer.toString();

            File save = new File("guildData.json");

            if (save.exists())
            {
                save.delete();
            }

            save.createNewFile();

            result = new JSONObject(theString);
            FileUtils.writeStringToFile(save, result.toString(4));
            EntityUtils.consume(entity2);

            return result.toString(4);
        } catch (JSONException e)
        {
            return null;
        }
    }

    public static void uploadData(String url, final ArrayList<User> users, TableView table)
    {
        new Thread(() ->
        {
            try
            {

                CloseableHttpClient httpclient = HttpClients.createDefault();

                Platform.runLater(() ->
                {
                    final FXMLController ctrl = MainApp.ctrl;
                    ctrl.btn_upload.setDisable(true);
                    table.setVisible(false);

                    MainApp.ctrl.prg_loading.setVisible(true);
                    MainApp.ctrl.lbl_loading.setVisible(true);
                });

                int num = 0;
                for (User u : users)
                {
                    num++;
                    final int num2 = num;
                    Platform.runLater(() ->
                    {
                        final FXMLController ctrl = MainApp.ctrl;
                        ctrl.lbl_loading.setText("Uploading record " + num2 + " of " + users.size());
                    });

                    JSONObject user = new JSONObject(u.getRawData());

                    String name = user.getString("Name");
                    int gold = user.getInt("GuildGoldContributedWeekly");
                    int seals = user.getInt("GuildSealsWeekly");
                    int trophies = user.getInt("GuildTrophiesWeekly");

                    System.out.println("User: " + name + ". Gold: " + gold + ". Seals: " + seals + ". Trophies: " + trophies);

                    HttpPost post_set_weekly_data = new HttpPost(url + "/api/setWeeklyStuff.php");

                    List<BasicNameValuePair> body = new ArrayList<>();
                    body.add(new BasicNameValuePair("name", name));
                    body.add(new BasicNameValuePair("gold", "" + gold));
                    body.add(new BasicNameValuePair("seals", "" + seals));
                    body.add(new BasicNameValuePair("trophies", "" + trophies));

                    post_set_weekly_data.setEntity(new UrlEncodedFormEntity(body));

                    try (CloseableHttpResponse response_set_weekly_data = httpclient.execute(post_set_weekly_data))
                    {
                        HttpEntity entity2 = response_set_weekly_data.getEntity();

                        EntityUtils.consume(entity2);
                    }

                    HttpPost post_set_profile_data = new HttpPost(url + "/api/updateProfile.php");

                    body = new ArrayList<>();
                    body.add(new BasicNameValuePair("name", name));
                    body.add(new BasicNameValuePair("profile", user.toString()));

                    post_set_profile_data.setEntity(new UrlEncodedFormEntity(body));

                    try (CloseableHttpResponse response_set_profile_data = httpclient.execute(post_set_profile_data))
                    {
                        HttpEntity entity2 = response_set_profile_data.getEntity();

                        EntityUtils.consume(entity2);
                    }

                }
            } catch (IOException e)
            {
                Utils.msgBox(Alert.AlertType.ERROR, "We couldn't upload your data. Please ensure the URL is correct.", OK);
            } finally
            {
                Platform.runLater(() ->
                {
                    final FXMLController ctrl = MainApp.ctrl;

                    table.setVisible(true);
                    MainApp.ctrl.prg_loading.setVisible(false);
                    MainApp.ctrl.lbl_loading.setVisible(false);

                    ctrl.btn_upload.setDisable(false);
                });
            }

        }).start();
    }
}
