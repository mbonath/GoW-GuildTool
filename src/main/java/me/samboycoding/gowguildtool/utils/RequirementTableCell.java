package me.samboycoding.gowguildtool.utils;

import javafx.scene.control.TableCell;
import me.samboycoding.gowguildtool.User;
import me.samboycoding.gowguildtool.files.ConfigFileManager;

/**
 * Represents a table cell with requirements
 *
 * @author Sam
 */
public class RequirementTableCell extends TableCell<User, Double>
{

    private int type;
    private double req;

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
    protected void updateItem(final Double item, final boolean empty)
    {
        super.updateItem(item, empty);
        setText(empty ? "" : "" + item.intValue());

        getStyleClass().removeAll("tablecell-bad", "tablecell-good");

        updateStyles(empty ? null : item);

    }

    private void updateStyles(Double item)
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

    public static class RequirementStore
    {

        public static double reqGold = -1;
        public static double reqSeals = -1;
        public static double reqTrophies = -1;

        public RequirementStore()
        {
            reqGold = ConfigFileManager.inst.getData().getJSONObject("Requirements").getDouble("Gold");

            reqSeals = ConfigFileManager.inst.getData().getJSONObject("Requirements").getDouble("Seals");

            reqTrophies = ConfigFileManager.inst.getData().getJSONObject("Requirements").getDouble("Trophies");
        }

    }
}
