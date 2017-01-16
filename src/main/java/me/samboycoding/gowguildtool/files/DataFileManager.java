package me.samboycoding.gowguildtool.files;

import java.io.File;
import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class to load the data file.
 * 
 * @author Sam
 */
public class DataFileManager 
{
    private String uName;
    private String pwd;
    public static DataFileManager inst;
    
    public DataFileManager()
    {
        inst = this;
    }
    
    public void loadFromJSON(File dataFile) throws Exception
    {
        String content = FileUtils.readFileToString(dataFile, "UTF-8");
        
        try
        {   
            JSONObject data = new JSONObject(content);
            
            uName = data.getString("username");
            pwd = data.getString("password");
        } catch(JSONException e)
        {
            throw e;
        }
        
    }
    
    public String getUsername()
    {
        return uName;
    }
    
    public String getPassword()
    {
        return pwd;
    }
}
