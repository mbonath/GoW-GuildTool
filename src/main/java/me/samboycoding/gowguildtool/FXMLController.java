package me.samboycoding.gowguildtool;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class FXMLController implements Initializable {
    
    @FXML
    public TabPane tabpane;
    @FXML
    public Tab tab_overview;
    @FXML
    public Tab tab_config;
    @FXML
    public Label lbl_config_filepath;
    @FXML
    public Label lbl_config_username;
    @FXML
    public Label lbl_config_password;
    @FXML
    public Label lbl_config_serverurl;
    @FXML
    public Button btn_browse_file;
    @FXML
    public Button btn_autodetect_file;
    @FXML
    public Button btn_refresh;
    @FXML
    public Button btn_upload;
    @FXML
    public TextField txt_datafile;
    @FXML
    public TextField txt_username;
    @FXML
    public TextField txt_password;
    @FXML
    public TextField txt_serverurl;
    @FXML
    public TextField txt_req_gold;
    @FXML
    public TextField txt_req_seals;
    @FXML
    public TextField txt_req_trophies;
    @FXML
    public Label lbl_req_gold;
    @FXML
    public Label lbl_req_seals;
    @FXML
    public Label lbl_req_trophies;
    @FXML
    public Button btn_save_requirements;
    @FXML
    public ProgressBar prg_loading;
    @FXML
    public Label lbl_loading;
    @FXML
    public Label lbl_no_score;
    @FXML
    public AnchorPane panel_overview;
    @FXML
    public AnchorPane panel_config;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
}
