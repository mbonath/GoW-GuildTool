package me.samboycoding.gowguildtool;

import com.sun.javafx.collections.ObservableListWrapper;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
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
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import me.samboycoding.gowguildtool.files.ConfigFileManager;
import me.samboycoding.gowguildtool.files.DataFileManager;
import me.samboycoding.gowguildtool.utils.Utils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.json.JSONArray;
import org.json.JSONObject;

public class MainApp extends Application
{

    FXMLController ctrl;
    DataFileManager m = new DataFileManager();

    EventHandler<WindowEvent> onLoad = new EventHandler<WindowEvent>()
    {
        @Override
        public void handle(WindowEvent event)
        {
            try
            {
                JSONObject data = ConfigFileManager.inst.getData();

                if (!data.isNull("FileLocation"))
                {
                    File dataFile = new File(data.getString("FileLocation"));
                    ctrl.txt_datafile.setText(dataFile.getAbsolutePath());

                    DataFileManager.inst.loadFromJSON(dataFile);

                    final String uname = DataFileManager.inst.getUsername();
                    final String pwd = DataFileManager.inst.getPassword();

                    ctrl.txt_username.setText(uname);
                    ctrl.txt_password.setText(pwd);

                    ctrl.lbl_loading.setText("Loading your user data...");

                    new Thread(() ->
                    {
                        try
                        {
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

                            TableView<User> table = new TableView<>();
                            ObservableList<User> userList = new ObservableListWrapper<>(new ArrayList<>());

                            JSONArray users = guilddata.getJSONObject("result").getJSONObject("GuildData").getJSONArray("Users");

                            for (Object o : users)
                            {
                                JSONObject u = (JSONObject) o;
                                User user = new User();

                                user.setUsername(u.getString("Name"));
                                user.setLevel(u.getInt("Level"));
                                user.setGold(u.getInt("GuildGoldContributedWeekly"));
                                user.setSeals(u.getInt("GuildSealsWeekly"));
                                user.setTrophies(u.getInt("GuildTrophiesWeekly"));

                                int gold = user.getGold();
                                int seals = user.getSeals();
                                int troph = user.getTrophies();

                                JSONObject req = conf.getJSONObject("Requirements");

                                if (req.getInt("Gold") != 0 && req.getInt("Seals") != 0 && req.getInt("Trophies") != 0)
                                {
                                    double score = Math.round((((user.getGold() / req.getInt("Gold")) * 0.3) + ((user.getSeals() / req.getInt("Seals")) * 0.4) + ((user.getTrophies() / req.getInt("Trophies") * 0.3))) * 100);
                                    
                                    user.setScore("" + score);
                                } else
                                {
                                    user.setScore("Requirements Not Set");
                                }
                                userList.add(user);
                            }

                            TableColumn<User, String> usernameCol = new TableColumn<>("Username");
                            usernameCol.setCellValueFactory(new PropertyValueFactory("username"));
                            TableColumn<User, Integer> levelCol = new TableColumn<>("Level");
                            levelCol.setCellValueFactory(new PropertyValueFactory("level"));
                            TableColumn<User, Integer> goldCol = new TableColumn<>("Gold Donated");
                            goldCol.setCellValueFactory(new PropertyValueFactory("gold"));
                            TableColumn<User, Integer> sealsCol = new TableColumn<>("Seals Earned");
                            sealsCol.setCellValueFactory(new PropertyValueFactory("seals"));
                            TableColumn<User, Integer> trophiesCol = new TableColumn<>("Trophies Earned");
                            trophiesCol.setCellValueFactory(new PropertyValueFactory("trophies"));
                            TableColumn<User, String> scoreCol = new TableColumn<>("User Score");
                            scoreCol.setCellValueFactory(new PropertyValueFactory("score"));

                            table.getColumns().setAll(usernameCol, levelCol, goldCol, sealsCol, trophiesCol, scoreCol);

                            Platform.runLater(() ->
                            {
                                ctrl.prg_loading.setVisible(false);
                                ctrl.lbl_loading.setVisible(false);

                                AnchorPane.setBottomAnchor(table, 1d);
                                AnchorPane.setLeftAnchor(table, 1d);
                                AnchorPane.setRightAnchor(table, 1d);
                                AnchorPane.setTopAnchor(table, 30d);
                                ctrl.panel_overview.getChildren().add(table);

                                table.setItems(userList);
                            });
                        } catch (IOException ex)
                        {
                            Utils.msgBoxThreadSafe(Alert.AlertType.ERROR, "Unable to load user data!", OK);
                        }
                    }).start();
                }
            } catch (Exception ex)
            {
                Utils.msgBox(Alert.AlertType.ERROR, "Unable to launch: " + ex.toString(), OK);
                Platform.exit();
            }
        }
    };

    @Override
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void start(Stage stage) throws Exception
    {
        FXMLLoader ldr = new FXMLLoader();
        Parent root = ldr.load(getClass().getResource("/fxml/Scene.fxml").openStream());
        ctrl = ldr.getController();

        new ConfigFileManager();

        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/Styles.css");

        stage.setTitle("GoW Guild Manager Client");
        stage.setScene(scene);

        ctrl.btn_autodetect_file.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
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
