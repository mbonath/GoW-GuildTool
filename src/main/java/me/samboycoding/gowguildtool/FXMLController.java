package me.samboycoding.gowguildtool;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;

public class FXMLController implements Initializable {
    
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
    public Button btn_browse_file;
    @FXML
    public Button btn_autodetect_file;
    @FXML
    public TextField txt_datafile;
    @FXML
    public TextField txt_username;
    @FXML
    public TextField txt_password;
    @FXML
    public ProgressBar prg_loading;
    @FXML
    public Label lbl_loading;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
}
