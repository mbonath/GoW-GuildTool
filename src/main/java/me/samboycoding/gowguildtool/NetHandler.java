package me.samboycoding.gowguildtool;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
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
        payload.put("ClientVersionNumber","2.2.007");

        String postData = payload.toString(4);
        
        CloseableHttpClient httpclient = HttpClients.createDefault();
        
        HttpPost post_get_userdata = new HttpPost("http://gemsofwar.parseapp.com/call_function");
        post_get_userdata.setEntity(new StringEntity(postData));
        post_get_userdata.setHeader("Content-Type", "application/json");
        
        JSONObject result;

        try (CloseableHttpResponse response_get_userdata = httpclient.execute(post_get_userdata))
        {
            HttpEntity entity2 = response_get_userdata.getEntity();

            StringWriter writer = new StringWriter();
            IOUtils.copy(entity2.getContent(), writer, (String) null);
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
        payload.put("ClientVersionNumber","2.2.007");

        String postData = payload.toString(4);
        
        CloseableHttpClient httpclient = HttpClients.createDefault();
        
        HttpPost post_get_guilddata = new HttpPost("http://gemsofwar.parseapp.com/call_function");
        post_get_guilddata.setEntity(new StringEntity(postData));
        post_get_guilddata.setHeader("Content-Type", "application/json");
        
        JSONObject result;

        try (CloseableHttpResponse response_get_guilddata = httpclient.execute(post_get_guilddata))
        {
            HttpEntity entity2 = response_get_guilddata.getEntity();

            StringWriter writer = new StringWriter();
            IOUtils.copy(entity2.getContent(), writer, (String) null);
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

    public static void uploadData(String string)
    {
        //TODO
    }
}
