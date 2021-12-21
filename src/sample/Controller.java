package sample;

import Scanner.Scan;
import Scanner.NonToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.File;
import java.net.URL;
import java.util.*;

import Scanner.Token;


public class Controller implements Initializable {

    static ObservableList<NonToken> listNonToken = FXCollections.observableArrayList();
    static ObservableList<Token> listToken = FXCollections.observableArrayList();

    //CodeArea codeArea;
    TextArea textArea = new TextArea();
    static String codeWithoutNonTokens = "";
    StringBuffer temp;
    private File f;
    boolean isFileOpen = false;
    @FXML
    Button btnNew;
    @FXML
    Button btnSave;
    @FXML
    Button btnClose;
    @FXML
    Button btnSaveAs;
    @FXML
    Button btnOpen;
    @FXML
    AnchorPane myPane;
    @FXML
    Button btnScan;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        textArea.setPrefSize(888, 540);
    }

    @FXML
    void clickScan() {
        try {
            boolean isString = false;
            listToken.clear();
            listNonToken.clear();
            temp = new StringBuffer(textArea.getText());

            codeWithoutNonTokens = temp.toString();
            StringTokenizer lineTokenizer = new StringTokenizer(codeWithoutNonTokens, "\n", true);
            int lineNo = 1;
            while (lineTokenizer.hasMoreTokens()) {
                String nextToken = lineTokenizer.nextToken();
                if (nextToken.equals("\n")) {
                    lineNo++;
                    continue;
                }
                //devide the input based on spaces
                StringTokenizer spaceTokenizer = new StringTokenizer(nextToken, " ");
                while (spaceTokenizer.hasMoreTokens()) {
                    String token = spaceTokenizer.nextToken();
                    if (Scan.isKeyword(token) && !isString)
                        //is it a Keyword ??
                        listToken.add(new Token(lineNo, "KEYWORD", token, token));
                    else {
                        for (int i = 0; i < token.length(); i++) {
                            String character = Character.toString(token.charAt(i));
                            //is it an operator or separator??
                            if ((Scan.isOperator(character) || Scan.isSeparator(character)) && !isString) {

                                if (((i + 1) < token.length()) && (character.equals("+") || character.equals("-") || (character.equals(":") && Character.toString(token.charAt(i + 1)).equals("=")) || character.equals("="))) {
                                    System.out.println("OPERATOR");
                                    String character2 = Character.toString(token.charAt(i + 1));

                                    //traiter le cas de :=
                                    if (character.equals(':') && character2.equals('=')) {
                                        listToken.add(new Token(lineNo, "MATH OPERATOR", "Assignment OP", character+character2));
                                        i++;
                                        continue;
                                    } else {
                                        listToken.add(new Token(lineNo, "MATH OPERATOR", Scan.getSymbolName(character), character));
                                        continue;
                                    }
                                } else {
                                    System.out.println("SEPARATOR");
                                    listToken.add(new Token(lineNo, "SEPARATOR", "SEP", character));
                                    //i++;
                                    continue;
                                }
                            }
                            // is it a identifier??
                            String tempToken = "";
                            if (Scan.isIdentifier(character) && !isString) {

                                do {
                                    tempToken += character;
                                    i++;
                                    if (i < token.length())
                                        character = Character.toString(token.charAt(i));
                                    else break;
                                } while (Scan.isIdentifier(character) || Scan.isNumber(character));
                                --i;
                                if (Scan.isKeyword(tempToken))
                                    listToken.add(new Token(lineNo, "KEYWORD", tempToken, tempToken));
                                else
                                    listToken.add(new Token(lineNo, "IDENTIFIER", "Identifier", tempToken));
                                continue;
                            }

                            if (Scan.isNumber(character) && !isString) {
                                boolean dotError = false;
                                tempToken = "";
                                do {
                                    tempToken += character;
                                    ++i;
                                    if (i < token.length()) {
                                        character = Character.toString(token.charAt(i));
                                        if (tempToken.contains(".") && character.equals(".")) {
                                            dotError = true;
                                            break;
                                        }
                                    } else break;
                                } while (Scan.isNumber(character) || character.equals("."));
                                listToken.add(new Token(lineNo, "CONSTANT", "const", tempToken));
                                if (dotError) {
                                    listToken.add(new Token(lineNo, "Error", "error", "error"));
                                    listToken.add(new Token(lineNo, "SPECIAL SYMBOL", "symbol", "."));
                                }
                                continue;
                            }
                            tempToken += "";

                            listToken.add(new Token(lineNo, "INVALID", "invalid", token.substring(token.indexOf(character))));
                            break;
                        }
                    }
                }
            }
            scannerOutputPage();


        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.toString());
        }
    }

    void scannerOutputPage() throws Exception {
        Parent root;
        Stage primaryStage = new Stage();
        root = FXMLLoader.load(getClass().getResource("ScannerOutput.fxml"));
        primaryStage.setTitle("Scanner Result");
        Scene scene = new Scene(root);
        scene.getStylesheets().add(Main.class.getResource("styling.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.show();
    }

    @FXML
    void clickNew() {
        myPane.getChildren().clear();
        myPane.getChildren().add(textArea);
        textArea.setText(sampleCode);
        textArea.setVisible(true);
        btnClose.setDisable(false);
        btnSaveAs.setDisable(false);
        isFileOpen = true;
        btnNew.setDisable(true);
        btnOpen.setDisable(true);
        btnScan.setDisable(false);
    }

    @FXML
    void clickOpen() {

        try {
            FileChooser chooser = new FileChooser();
            f = chooser.showOpenDialog(null);
            //Project extension (*.compil)
            if (!(f.getName()).contains(".compil")) throw new Exception();
            TempFile file = new TempFile();
            if (file.input(f)) {
                myPane.getChildren().clear();
                myPane.getChildren().add(textArea);
                textArea.setText(file.getFiletext());
                textArea.setVisible(true);
                btnScan.setDisable(false);
                btnSave.setDisable(false);
                btnClose.setDisable(false);
                btnSaveAs.setDisable(false);
                isFileOpen = true;
                btnNew.setDisable(true);

            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Cant Open File");
        }
    }

    @FXML
    void clickSave() {
        try {
            TempFile file = new TempFile();
            file.setFiletext(textArea.getText());
            file.output(f);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Cant Save File");
        }
    }

    @FXML
    void clickSaveAs() {
        try {
            FileChooser chooser = new FileChooser();
            File f = chooser.showSaveDialog(null);
            TempFile file = new TempFile();
            file.setFiletext(textArea.getText());
            file.output(f);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Cant Save File");
        }
    }

    @FXML
    void clickClose() {
        onclose("close");
        btnScan.setDisable(true);
        btnSaveAs.setDisable(true);
        btnSave.setDisable(true);
        btnClose.setDisable(false);
        btnNew.setDisable(false);
        btnOpen.setDisable(false);
    }

    @FXML
    void clickExit() {
        onclose("exit");
        btnNew.setDisable(false);
    }

    void onclose(String button) {
        if (!isFileOpen && button == "exit")
            System.exit(0);
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Save");
            alert.setHeaderText("Save Change Before Exit");
            ButtonType btnTSave = new ButtonType("Yes");
            ButtonType btnTDontSave = new ButtonType("No");
            ButtonType btnTCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(btnTSave, btnTDontSave, btnTCancel);
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == btnTSave)

                clickSaveAs();
            if (button == "close") {
                textArea.clear();
                textArea.setVisible(false);
                isFileOpen = false;
            } else {
                System.exit(0);
            }
        }
    }

    private static final String sampleCode = String.join("\n", new String[]{
            "PROGRAM test",
            "VAR n : INT",
            "FUNCTION fib( i : INT , j : INT ) : INT",
            "BEGIN IF n=0 THEN RETURN i",
            "   ELSE IF n=1 THEN RETURN j",
            "      ELSE BEGIN n := n - 1 ; fib ( j, i+j ) END",
            "END ;"
    });


}