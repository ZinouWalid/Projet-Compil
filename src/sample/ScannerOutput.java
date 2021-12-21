package sample;

import Scanner.NonToken;
import Scanner.Token;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class ScannerOutput implements Initializable {
    //Token Table
    @FXML
    TableView<Token> tokenTable;
    @FXML
    TableColumn<Token, String> categoryColumn;
    @FXML
    TableColumn<Token, String> tNameColumn;
    @FXML
    TableColumn<Token, String> tValueColumn;
    @FXML
    TableColumn<Token, Integer> tLineNoColumn;
    @FXML
    Label lblTotalToken;
    @FXML
    Label lblTotalLiteral;
    // Non Token Table
    @FXML
    TableView<NonToken> nonTokenTable;
    @FXML
    TableColumn<NonToken, String> nonTokenNameColumn;
    @FXML
    TableColumn<NonToken, String> nonTokenValueColumn;
    @FXML
    Label lblNonTokenTotal;
    //code area without nonTokens
    @FXML
    TextArea txtAreaCodeWithoutNonTokens;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Initialize Token Table
        tLineNoColumn.setCellValueFactory(
                new PropertyValueFactory<Token, Integer>("lineNo"));
        categoryColumn.setCellValueFactory(
                new PropertyValueFactory<Token, String>("catagoy"));
        tNameColumn.setCellValueFactory(
                new PropertyValueFactory<Token, String>("name"));
        tValueColumn.setCellValueFactory(
                new PropertyValueFactory<Token, String>("value"));
        tokenTable.getItems().setAll(Controller.listToken);
        lblTotalToken.setText("Total Token are " + Controller.listToken.size());
        //Initialize Literal Table

        //Initialize Non token Table

        //fill text Area with code
        txtAreaCodeWithoutNonTokens.setText(Controller.codeWithoutNonTokens);

    }

}
