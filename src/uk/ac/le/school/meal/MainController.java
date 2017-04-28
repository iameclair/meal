package uk.ac.le.school.meal;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;

import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.commons.collections4.IteratorUtils;

import org.apache.commons.collections4.ListUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;


import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainController implements Initializable{

    private Map<String, File> fileMap = new HashMap<>();
    private List<School> listOfSchools = new ArrayList<>();
    private String[] months = {"January","February", "March", "April", "May", "June", "Jully", "September", "October", "November", "December"};
    private ObservableList<School> tableData;
    private ObservableList listData;
    private List<String> stringArray = new ArrayList<>();

    @FXML private Button browseBtn;
    @FXML private Button queryBtn;
    @FXML private Button hideScene;
    @FXML private GridPane resultScene;
    @FXML private Label errorLabel;
    @FXML private ListView listView;
    @FXML private TableView<School> tableView;
    @FXML private TextField queryBox;
    @FXML private TextField showFileTextView;
    @FXML private TableColumn<School, String> nameColumn;
    @FXML private TableColumn<School, String> monthColumn;
    @FXML private TableColumn<School, Integer> nPupilsColumn;
    @FXML private TableColumn<School, Integer> noEligible;

    @FXML
    public void browseFiles(ActionEvent click){
        //browse for excel files open them
        final FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter fileExtensions = new FileChooser.ExtensionFilter(
                        "Excel files", "*.xlsx", "*.xls");
        fileChooser.getExtensionFilters().addAll(fileExtensions);
        List<File> files = fileChooser.showOpenMultipleDialog(null);
        if(files != null){
            for(int i = 0; i < files.size(); i++){
                showFileTextView.appendText(files.get(i).getAbsolutePath());
                if(i != files.size()-1){
                    showFileTextView.appendText(" , ");
                }
                String s = files.get(i).getName();
                fileMap.put(s, files.get(i));
            }
        }
        //call the method tha will read the files
        try {
            readFromExcel(fileMap);
        } catch (IOException e) {
            showFileTextView.setText("The process cannot access the file because it is being used by another process");
            e.printStackTrace();
        } catch (InvalidFormatException e){
            showFileTextView.setText("Invalid file format");
            e.printStackTrace();
        }

        if(listOfSchools == null)errorLabel.setText("file wrong format can't read the file");
        populateTable(listOfSchools);
    }

    /**
     * read the excel files and arrang the data and store the information in a file
     * @param map
     * @throws IOException
     * @throws InvalidFormatException
     */
    public void readFromExcel(Map<String, File> map) throws IOException, InvalidFormatException {
       Workbook workbook = null;
       String month = "";
       School school = new School();

        for(Map.Entry<String, File> entry: map.entrySet()){
            month = entry.getKey();
            workbook = WorkbookFactory.create(entry.getValue());

            //create a sheet objcet to retrieve the sheet
            for(int i = 0; i < workbook.getNumberOfSheets(); i++){
                Sheet sheet = workbook.getSheetAt(i);
                //create an iterator for the row
                Iterator<Row> rowIterator = sheet.iterator();
                //convert iterator into list
                List<Row> rowList = IteratorUtils.toList(rowIterator);

                //iterate through the list of rows
                for(int j = 4; j < rowList.size(); j++){
                    //only read school names

                    Row row = rowList.get(j);
                    //iterator for the cell
                    Iterator<Cell> cellIterator = row.cellIterator();
                    //convert to list
                    List<Cell> cellList = IteratorUtils.toList(cellIterator);
                    //iterate through the cell
                    for(int c = 1; c < cellList.size(); c++){
                        Cell cell = cellList.get(c);
                        if(c == 1){
                            String s = month.substring(month.lastIndexOf("- "));
                            String s1 = null;
                            for(String m: months){
                                if(s.toLowerCase().contains(m.toLowerCase())){
                                    String regex = m+" \\d{4}";
                                    Pattern pattern = Pattern.compile("("+regex+")");
                                    Matcher matcher = pattern.matcher(s);
                                    if (matcher.find())
                                    {
                                        s1 = matcher.group(1);
                                    }
                                }
                            }
                            school.setMonthProperty(s1);
                            switch(cell.getCellType()){
                                case Cell.CELL_TYPE_STRING:
                                    school.setNameProperty(cell.getStringCellValue());
                                    break;
                            }

                        }
                        if(c == 5){
                            int n = (int) cell.getNumericCellValue();
                            school.setNStudentOnRollProperty(n);
                        }
                        if(c == 6){
                            school.setNStudentEligibleProperty((int) cell.getNumericCellValue());
                        }

                    }
                    School toAdd = new School(school.getMonthProperty(),school.getNameProperty(),school.getNStudentOnRollProperty(),school.getNStudentEligibleProperty());
                    if(!toAdd.getNameProperty().contains("Calderdale")) {
                        if(!toAdd.getNameProperty().equals("")) {
                            listOfSchools.add(toAdd);
                        }
                    }
                }
            }
            workbook.close();
        }
    }

    //populate table
    public void populateTable(List<School> list){
        tableData = FXCollections.observableArrayList(list);
        tableView.setItems(tableData);
    }

    //query results
    @FXML
    public void queryData(ActionEvent event) throws IOException,NullPointerException{
        Map<String, Integer> resultMap = new HashMap<String, Integer>();
        List<School> results = new ArrayList<>();
       String text = queryBox.getText();
       int textInt = Integer.parseInt(text);
       for(int i = 0; i < listOfSchools.size(); i++){
           if(listOfSchools.get(i).getNStudentOnRollProperty() >= textInt){
               results.add(listOfSchools.get(i));
           }
       }
       //
        List<String> keys = new ArrayList<>();
        String key = results.get(0).getMonthProperty();
        keys.add(key);
       for(int j = 1; j < results.size(); j++){
           if(!results.get(j-1).getMonthProperty().equals(results.get(j).getMonthProperty())){
               key = results.get(j).getMonthProperty();
               keys.add(key);
           }

       }
       //
        for(int k = 0; k < keys.size(); k++){
           Integer value = 0;
            for(int c = 0; c < results.size(); c++){
                if(results.get(c).getMonthProperty().equals(keys.get(k))){
                    value++;
                }
            }
            resultMap.put(keys.get(k), value);
        }

       //
        for(Map.Entry<String, Integer> entry: resultMap.entrySet()){
            String query = entry.getKey()+" "+entry.getValue();
            stringArray.add(query);
        }

        listData = FXCollections.observableArrayList(stringArray);
        listView.setItems(listData);

    }


    /**
     * initializez the table view
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        monthColumn.setCellValueFactory(new PropertyValueFactory<School, String>("monthProperty"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<School, String>("nameProperty"));
        nPupilsColumn.setCellValueFactory(new PropertyValueFactory<School, Integer>("nStudentOnRollProperty"));
        noEligible.setCellValueFactory(new PropertyValueFactory<School, Integer>("nStudentEligibleProperty"));
    }

}
