package me.samboycoding.gowguildtool;

import com.sun.javafx.collections.ObservableListWrapper;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import static javafx.scene.control.ButtonType.CLOSE;
import static javafx.scene.control.ButtonType.OK;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import me.samboycoding.gowguildtool.files.ConfigFileManager;
import me.samboycoding.gowguildtool.files.DataFileManager;
import me.samboycoding.gowguildtool.utils.RequirementTableCell;
import me.samboycoding.gowguildtool.utils.Utils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainApp extends Application
{

    public static FXMLController ctrl;
    DataFileManager m = new DataFileManager();
    TableView<User> table = null;
    public static ArrayList<User> users = new ArrayList<>();

    private void doLoadData()
    {
        try
        {
            JSONObject data = ConfigFileManager.inst.getData();

            if (!data.isNull("FileLocation"))
            {
                File dataFile = new File(data.getString("FileLocation"));
                ctrl.txt_datafile.setText(dataFile.getAbsolutePath());

                try
                {
                    DataFileManager.inst.loadFromJSON(dataFile);
                } catch (Exception ex)
                {
                    Utils.msgBox(Alert.AlertType.WARNING, "We couldn't load the data from your file. Was it deleted or corrupted?", OK);
                    return;
                }

                final String uname = DataFileManager.inst.getUsername();
                final String pwd = DataFileManager.inst.getPassword();

                ctrl.txt_username.setText(uname);
                ctrl.txt_password.setText(pwd);

                ctrl.lbl_loading.setText("Loading your user data...");

                new Thread(() ->
                {
                    try
                    {
                        if (table != null)
                        {
                            Platform.runLater(() ->
                            {
                                ((AnchorPane) table.getParent()).getChildren().remove(table);
                                table = null;
                            });
                        }
                        Platform.runLater(() ->
                        {
                            ctrl.btn_refresh.setDisable(true);
                            ctrl.btn_upload.setDisable(true);
                        });

                        ctrl.prg_loading.setVisible(true);
                        ctrl.lbl_loading.setVisible(true);
                        JSONObject conf = ConfigFileManager.inst.getData();
                        JSONObject userdata = new JSONObject(NetHandler.loadUserData(uname, pwd));

                        Platform.runLater(() ->
                        {
                            ctrl.lbl_loading.setText("Loading Guild Data...");
                        });

                        JSONObject guilddata = new JSONObject(NetHandler.loadGuildData(userdata));

                        if (guilddata.has("error"))
                        {
                            if (guilddata.getString("error").contains("NOT_IN_A_GUILD"))
                            {
                                Utils.msgBoxThreadSafe(Alert.AlertType.ERROR, "We couldn't load guild data, because you are not in a guild.", OK);
                                return;
                            }
                        }

                        Platform.runLater(() ->
                        {
                            ctrl.lbl_loading.setText("Rendering...");
                        });

                        table = new TableView<>();
                        ObservableList<User> userList = new ObservableListWrapper<>(new ArrayList<>());

                        JSONArray rawUsers = guilddata.getJSONObject("result").getJSONObject("GuildData").getJSONArray("Users");
                        users.clear();
                        for (Object o : rawUsers)
                        {
                            JSONObject u = (JSONObject) o;
                            User user = new User();

                            user.setUsername(u.getString("Name"));
                            user.setLevel(u.getInt("Level"));
                            user.setGold(u.getDouble("GuildGoldContributedWeekly"));
                            user.setSeals(u.getDouble("GuildSealsWeekly"));
                            user.setTrophies(u.getDouble("GuildTrophiesWeekly"));
                            user.setRawData(u.toString());

                            double gold = user.getGold();
                            double seals = user.getSeals();
                            double troph = user.getTrophies();

                            JSONObject req = conf.getJSONObject("Requirements");

                            if (req.getDouble("Gold") != 0 && req.getDouble("Seals") != 0 && req.getDouble("Trophies") != 0)
                            {
                                double score = Math.round((((gold / req.getDouble("Gold")) * 0.3d) + ((seals / req.getDouble("Seals")) * 0.4d) + ((troph / req.getDouble("Trophies") * 0.3d))) * 100d);

                                user.setScore(((int) score));
                                ctrl.lbl_no_score.setVisible(false);
                            } else
                            {
                                user.setScore(-1);
                                ctrl.lbl_no_score.setVisible(true);
                            }
                            users.add(user);
                            userList.add(user);
                        }

                        TableColumn<User, String> usernameCol = new TableColumn<>("Username");
                        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));

                        TableColumn<User, Integer> levelCol = new TableColumn<>("Level");
                        levelCol.setCellValueFactory(new PropertyValueFactory<>("level"));

                        TableColumn<User, Double> goldCol = new TableColumn<>("Gold Donated");
                        goldCol.setCellValueFactory(new PropertyValueFactory<>("gold"));
                        goldCol.setCellFactory((final TableColumn<User, Double> personStringTableColumn) ->
                        {
                            return new RequirementTableCell(0);
                        });

                        TableColumn<User, Double> sealsCol = new TableColumn<>("Seals Earned");
                        sealsCol.setCellValueFactory(new PropertyValueFactory<>("seals"));
                        sealsCol.setCellFactory((final TableColumn<User, Double> personStringTableColumn) ->
                        {
                            return new RequirementTableCell(1);
                        });

                        TableColumn<User, Double> trophiesCol = new TableColumn<>("Trophies Earned");
                        trophiesCol.setCellValueFactory(new PropertyValueFactory<>("trophies"));
                        trophiesCol.setCellFactory((final TableColumn<User, Double> personStringTableColumn) ->
                        {
                            return new RequirementTableCell(2);
                        });

                        TableColumn<User, Integer> scoreCol = new TableColumn<>("User Score");
                        scoreCol.setCellValueFactory(new PropertyValueFactory<>("score"));

                        table.getColumns().setAll(usernameCol, levelCol, goldCol, sealsCol, trophiesCol, scoreCol);

                        Platform.runLater(() ->
                        {
                            ctrl.prg_loading.setVisible(false);
                            ctrl.lbl_loading.setVisible(false);

                            AnchorPane.setBottomAnchor(table, 1d);
                            AnchorPane.setLeftAnchor(table, 1d);
                            AnchorPane.setRightAnchor(table, 1d);
                            AnchorPane.setTopAnchor(table, 50d);
                            ctrl.panel_overview.getChildren().add(table);

                            table.setItems(userList);
                            ctrl.btn_refresh.setDisable(false);
                            ctrl.btn_upload.setDisable(false);
                        });
                    } catch (IOException ex)
                    {
                        Utils.msgBoxThreadSafe(Alert.AlertType.ERROR, "Unable to load user data!", OK);
                    }
                }).start();
            }
        } catch (Exception ex)
        {
            Utils.msgBox(Alert.AlertType.ERROR, "Unable to load data: " + ex.toString(), OK);
            Platform.exit();
        }
    }

    @SuppressWarnings("unchecked")
    EventHandler<WindowEvent> onLoad = ((WindowEvent event) ->
    {
        doLoadData();
    });

    @Override
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void start(Stage stage) throws Exception
    {
        FXMLLoader ldr = new FXMLLoader();
        Parent root = ldr.load(getClass().getResource("/fxml/Scene.fxml").openStream());
        ctrl = ldr.getController();

        new ConfigFileManager();

        JSONObject requimnts = ConfigFileManager.inst.getData().getJSONObject("Requirements");

        ctrl.txt_req_gold.setText(requimnts.getInt("Gold") + "");
        ctrl.txt_req_seals.setText(requimnts.getInt("Seals") + "");
        ctrl.txt_req_trophies.setText(requimnts.getInt("Trophies") + "");
        ctrl.lbl_no_score.setVisible(false);
        if (!ConfigFileManager.inst.getData().isNull("ServerURL"))
        {
            ctrl.txt_serverurl.setText(ConfigFileManager.inst.getData().getString("ServerURL"));
        }

        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/Styles.css");

        stage.setTitle("GoW Guild Manager Client");
        stage.setScene(scene);

        ctrl.btn_autodetect_file.setOnAction((ActionEvent event) ->
        {
            try
            {
                String path = System.getenv("appdata");
                if (path.isEmpty())
                {
                    Utils.msgBox(Alert.AlertType.WARNING, "Either you are not on windows, or your account is HORRIBLY broken. Please use a valid version of windows.", CLOSE);
                    Platform.exit();
                }
                File dir = new File(path + "/com.infinite-interactive.GoW/Local Store");
                if (!dir.exists())
                {
                    Utils.msgBox(Alert.AlertType.WARNING, "Auto-Detection Failed. Reason: GoW not installed or never run.", OK);
                    return;
                }

                File[] dataFiles = dir.listFiles((FilenameFilter) new WildcardFileFilter("User*.json"));

                if (dataFiles.length < 1)
                {
                    Utils.msgBox(Alert.AlertType.WARNING, "Auto-Detection Failed. Reason: GoW installed but not signed in.", OK);
                    return;
                }

                File dataFile = dataFiles[0];
                ctrl.txt_datafile.setText(dataFile.getAbsolutePath());

                DataFileManager.inst.loadFromJSON(dataFile);

                ctrl.txt_username.setText(DataFileManager.inst.getUsername());
                ctrl.txt_password.setText(DataFileManager.inst.getPassword());

                JSONObject data = ConfigFileManager.inst.getData();
                data.put("FileLocation", dataFile.getAbsolutePath());
                ConfigFileManager.inst.updateData(data);
            } catch (Exception ex)
            {
                Utils.msgBox(Alert.AlertType.ERROR, "Auto-Detection Failed. Reason: " + ex.toString(), OK);
                ex.printStackTrace();
            }
        });

        ctrl.btn_browse_file.setOnAction((ActionEvent event) ->
        {
            FileChooser chsr = new FileChooser();
            chsr.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("JSON File", "json"));

            File chosen = chsr.showOpenDialog(stage);

            if (chosen == null)
            {
                //User cancelled.
                return;
            }
            try
            {
                JSONObject content = new JSONObject(FileUtils.readFileToString(chosen));

                if (!content.has("username") || !content.has("password") || content.isNull("username") || content.isNull("password"))
                {
                    Utils.msgBox(Alert.AlertType.ERROR, "The file you selected is not a GoW JSON file, just a 'regular' JSON file.", OK);
                    return;
                }

                ctrl.txt_datafile.setText(chosen.getAbsolutePath());

                DataFileManager.inst.loadFromJSON(chosen);

                ctrl.txt_username.setText(DataFileManager.inst.getUsername());
                ctrl.txt_password.setText(DataFileManager.inst.getPassword());

                JSONObject data = ConfigFileManager.inst.getData();
                data.put("FileLocation", chosen.getAbsolutePath());
                ConfigFileManager.inst.updateData(data);
            } catch (JSONException e)
            {
                Utils.msgBox(Alert.AlertType.ERROR, "The file you selected contains invalid JSON.", OK);
            } catch (IOException ex)
            {
                Utils.msgBox(Alert.AlertType.ERROR, "Unable to read the specified file.", OK);
            } catch (Exception ex)
            {
                Utils.msgBox(Alert.AlertType.ERROR, "Unknown error reading file.", OK);
                ex.printStackTrace();
            }
        });

        ctrl.btn_save_requirements.setOnAction((ActionEvent event) ->
        {
            try
            {
                int gold = Integer.parseInt(ctrl.txt_req_gold.getText());
                int seals = Integer.parseInt(ctrl.txt_req_seals.getText());
                int trophies = Integer.parseInt(ctrl.txt_req_trophies.getText());

                if (gold < 0 || seals < 0 || trophies < 0)
                {
                    Utils.msgBox(Alert.AlertType.ERROR, "Invalid requirement! Must all be whole, positive numbers.", OK);
                    return;
                }

                JSONObject cnf = ConfigFileManager.inst.getData();
                JSONObject req = cnf.getJSONObject("Requirements");

                req.put("Gold", gold);
                req.put("Seals", seals);
                req.put("Trophies", trophies);

                cnf.put("Requirements", req);
                ConfigFileManager.inst.updateData(cnf);
            } catch (NumberFormatException nfe)
            {
                Utils.msgBox(Alert.AlertType.ERROR, "Invalid requirement! Must all be whole, positive numbers.", OK);
                ctrl.txt_req_gold.clear();
                ctrl.txt_req_seals.clear();
                ctrl.txt_req_trophies.clear();
            } catch (IOException ex)
            {
                Utils.msgBox(Alert.AlertType.ERROR, "Failed to save.", OK);
            }
        });

        ctrl.btn_refresh.setOnAction((ActionEvent event) ->
        {
            doLoadData();
        });

        ctrl.txt_serverurl.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) ->
        {
            if (!newPropertyValue)
            {
                try
                {
                    //Unfocused
                    JSONObject data = ConfigFileManager.inst.getData();
                    data.put("ServerURL", ctrl.txt_serverurl.getText());
                    ConfigFileManager.inst.updateData(data);
                } catch (IOException ex)
                {
                    //Just fail.
                }
            }
        });

        ctrl.btn_upload.setOnAction((ActionEvent event) ->
        {
            if (!ConfigFileManager.inst.getData().isNull("ServerURL"))
            {
                NetHandler.uploadData(ConfigFileManager.inst.getData().getString("ServerURL"), users, table);
            } else
            {
                Utils.msgBox(Alert.AlertType.WARNING, "You have no URL defined. Please define a URL", OK);
            }
        });

        ctrl.lbl_loading.setAlignment(Pos.CENTER);

        if (ConfigFileManager.inst.getData().isNull("FileLocation"))
        {
            ctrl.prg_loading.setVisible(false);
            ctrl.lbl_loading.setText("Not yet configured. Please go to the Configuration Tab.");
        }

        stage.setOnShown(onLoad);
        stage.show();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        launch(args);
    }

}