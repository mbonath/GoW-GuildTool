package me.samboycoding.gowguildtool.utils;

import javafx.scene.control.TableCell;
import me.samboycoding.gowguildtool.MainApp;
import me.samboycoding.gowguildtool.User;
import me.samboycoding.gowguildtool.files.ConfigFileManager;

/**
 * Represents a table cell with requirements
 *
 * @author Sam
 */
public class RequirementTableCell extends TableCell<User, Integer>
{

    private int type;
    private int req;

    /**
     * Constructor
     *
     * @param type The type of cell this is: 0: Gold 1: Seals 2: Trophies
     */
    public RequirementTableCell(int type)
    {
        this.type = type;
        if (RequirementStore.reqGold == -1)
        {
            new RequirementStore();
        }
        switch (this.type)
        {
            case 0:
                //Gold
                req = RequirementStore.reqGold;
                break;
            case 1:
                //Seals
                req = RequirementStore.reqSeals;
                break;
            case 2:
                //Trophies
                req = RequirementStore.reqTrophies;
                break;
        }
    }

    @Override
    protected void updateItem(final Integer item, final boolean empty)
    {
        super.updateItem(item, empty);
        setText(empty ? "" : item.toString());

        getStyleClass().removeAll("tablecell-bad", "tablecell-good");

        updateStyles(empty ? null : item);

    }

    private void updateStyles(Integer item)
    {
        if (item == null)
        {
            return;
        }

        if (req == 0)
        {
            return;
        }
        if (item < req)
        {
            getStyleClass().add("tablecell-bad");
        } else
        {
            getStyleClass().add("tablecell-good");
        }
    }

    private static class RequirementStore
    {

        public static int reqGold = -1;
        public static int reqSeals = -1;
        public static int reqTrophies = -1;

        public RequirementStore()
        {
            reqGold = ConfigFileManager.inst.getData().getJSONObject("Requirements").getInt("Gold");

            reqSeals = ConfigFileManager.inst.getData().getJSONObject("Requirements").getInt("Seals");

            reqTrophies = ConfigFileManager.inst.getData().getJSONObject("Requirements").getInt("Trophies");
        }

    }
}
