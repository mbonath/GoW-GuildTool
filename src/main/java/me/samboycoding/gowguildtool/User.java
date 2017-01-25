package me.samboycoding.gowguildtool;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
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

    @Override
    public String toString()
    {
        return "User {name=" + username.get() + "level=" + level.get() + ", gold=" + gold.get() + ", trophies=" + trophies.get() + ", seals=" + seals.get() + ", goldall=" + goldall.get() + ", trophiesall=" + trophiesall.get() + "}";
    }
    
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
    
    private DoubleProperty gold;

    public void setGold(double value)
    {
        goldProperty().set(value);
    }

    public double getGold()
    {
        return goldProperty().get();
    }

    public DoubleProperty goldProperty()
    {
        if (gold == null)
        {
            gold = new SimpleDoubleProperty(this, "gold");
        }
        return gold;
    }
    
    private DoubleProperty seals;

    public void setSeals(double value)
    {
        sealsProperty().set(value);
    }

    public double getSeals()
    {
        return sealsProperty().get();
    }

    public DoubleProperty sealsProperty()
    {
        if (seals == null)
        {
            seals = new SimpleDoubleProperty(this, "seals");
        }
        return seals;
    }
    
    private DoubleProperty trophies;

    public void setTrophies(double value)
    {
        trophiesProperty().set(value);
    }

    public double getTrophies()
    {
        return trophiesProperty().get();
    }

    public DoubleProperty trophiesProperty()
    {
        if (trophies == null)
        {
            trophies = new SimpleDoubleProperty(this, "trophies");
        }
        return trophies;
    }
    
    private IntegerProperty score;

    public void setScore(int value)
    {
        scoreProperty().set(value);
    }

    public int getScore()
    {
        return scoreProperty().get();
    }

    public IntegerProperty scoreProperty()
    {
        if (score == null)
        {
            score = new SimpleIntegerProperty(this, "score");
        }
        return score;
    }
	private DoubleProperty goldall;

    public void setGoldall(double value)
    {
        goldallProperty().set(value);
    }

    public double getGoldall()
    {
        return goldallProperty().get();
    }

    public DoubleProperty goldallProperty()
    {
        if (goldall == null)
        {
            goldall = new SimpleDoubleProperty(this, "goldall");
        }
        return goldall;
    }
	private DoubleProperty trophiesall;

    public void setTrophiesall(double value)
    {
        trophiesallProperty().set(value);
    }

    public double getTrophiesall()
    {
        return trophiesallProperty().get();
    }

    public DoubleProperty trophiesallProperty()
    {
        if (trophiesall == null)
        {
            trophiesall = new SimpleDoubleProperty(this, "trophiesall");
        }
        return trophiesall;
    }
}
