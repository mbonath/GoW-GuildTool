package me.samboycoding.gowguildtool;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Represents a user in the guild
 *
 * @author r3byass
 */
public class User
{

    private StringProperty username;

    public void setUsername(String value)
    {
        usernameProperty().set(value);
    }

    public String getUsername()
    {
        return usernameProperty().get();
    }

    public StringProperty usernameProperty()
    {
        if (username == null)
        {
            username = new SimpleStringProperty(this, "username");
        }
        return username;
    }
    
    private StringProperty rawData;

    public void setRawData(String value)
    {
        rawDataProperty().set(value);
    }

    public String getRawData()
    {
        return rawDataProperty().get();
    }

    public StringProperty rawDataProperty()
    {
        if (rawData == null)
        {
            rawData = new SimpleStringProperty(this, "username");
        }
        return rawData;
    }
    
    private IntegerProperty level;

    public void setLevel(int value)
    {
        levelProperty().set(value);
    }

    public int getLevel()
    {
        return levelProperty().get();
    }

    public IntegerProperty levelProperty()
    {
        if (level == null)
        {
            level = new SimpleIntegerProperty(this, "level");
        }
        return level;
    }
    
    private IntegerProperty gold;

    public void setGold(int value)
    {
        goldProperty().set(value);
    }

    public int getGold()
    {
        return goldProperty().get();
    }

    public IntegerProperty goldProperty()
    {
        if (gold == null)
        {
            gold = new SimpleIntegerProperty(this, "gold");
        }
        return gold;
    }
    
    private IntegerProperty seals;

    public void setSeals(int value)
    {
        sealsProperty().set(value);
    }

    public int getSeals()
    {
        return sealsProperty().get();
    }

    public IntegerProperty sealsProperty()
    {
        if (seals == null)
        {
            seals = new SimpleIntegerProperty(this, "seals");
        }
        return seals;
    }
    
    private IntegerProperty trophies;

    public void setTrophies(int value)
    {
        trophiesProperty().set(value);
    }

    public int getTrophies()
    {
        return trophiesProperty().get();
    }

    public IntegerProperty trophiesProperty()
    {
        if (trophies == null)
        {
            trophies = new SimpleIntegerProperty(this, "trophies");
        }
        return trophies;
    }
    
    private StringProperty score;

    public void setScore(String value)
    {
        scoreProperty().set(value);
    }

    public String getScore()
    {
        return scoreProperty().get();
    }

    public StringProperty scoreProperty()
    {
        if (score == null)
        {
            score = new SimpleStringProperty(this, "score");
        }
        return score;
    }
}
