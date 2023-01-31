package pages;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import utilities.Driver;

import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class foodSite_pageObject extends Driver {

    @FindBy(css = "#recipe h1")
    public WebElement FoodHeaderName;

    @FindBy(xpath = "//dt[text()='Ready In:']/..//dd")
    public WebElement readyMin;

    @FindBy(xpath = "//ul[contains(@class,'direction-list')]")
    public WebElement directionList;

    @FindBy(xpath = "//ul[contains(@class,'ingredient-list')]")
    public WebElement integrationList;

    @FindBy(xpath = "//button[contains(@class,'facts__nutrition')]")
    public WebElement nutritionInfoLink;

    @FindBy(xpath = "//div[contains(@aria-labelledby,'nutrition-info')]")
    public WebElement nutritionPopUpInfo;

    public static String strFoodHeader, strReadyInMin, strIntegrationList, strDirectionList, strNutritionInfo;


    public foodSite_pageObject(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public void loginCheck() {
        int k = 0, column = 0;
        Cell value = null;
        Row rowValue = null;
        String UrlValue = null;
        try {
            //excelData();
            File excelFile = new File("src/test/resources/FOODDATA.xlsx");
            FileInputStream fis = new FileInputStream(excelFile);

            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIt = sheet.iterator();
            int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();
            for (int i = 1; i <= rowCount; i++) {
                Thread.sleep(5000);
                int cellCount = sheet.getRow(i).getLastCellNum();
                log.info("cell count is ..." + cellCount);
                for (int j = 0; j < cellCount; j++) {

                    log.info(sheet.getRow(i).getCell(j).getStringCellValue());
                    UrlValue = sheet.getRow(i).getCell(j).getStringCellValue();
                    visit("https://www.food.com/recipe/" + UrlValue);
                    waitForVisibility(FoodHeaderName, 30);
                    Assert.assertTrue(true);
                    headerValue();
                    readyInMin();
                    getDirectionList();
                    getIntegrationList();
                    getNutritionValue();
                    XSSFCell cellHeader = sheet.getRow(i).createCell(1);
                    cellHeader.setCellValue(strFoodHeader);
                    XSSFCell cellReadyMin = sheet.getRow(i).createCell(2);
                    cellReadyMin.setCellValue(strReadyInMin);
                    XSSFCell cellDirectionList = sheet.getRow(i).createCell(3);
                    cellDirectionList.setCellValue(strDirectionList);
                    XSSFCell cellIntegrationValue = sheet.getRow(i).createCell(4);
                    cellIntegrationValue.setCellValue(strIntegrationList);
                    XSSFCell cellNutritionValue = sheet.getRow(i).createCell(5);
                    cellNutritionValue.setCellValue(strNutritionInfo);

                    FileOutputStream ots = new FileOutputStream(excelFile);
                    workbook.write(ots);
                    Thread.sleep(5000);
                }
            }
            workbook.close();
//            Row firstRow = rowIt.next();
//            Iterator<Cell> cellIt =  firstRow.cellIterator();
//            while (cellIt.hasNext()){
//                 value = cellIt.next();
//                if (value.getStringCellValue().equalsIgnoreCase("URL")){
//                    column=k;
//                }
//                k++;
//            }
//            int cellCount = sheet.getRow(column).getLastCellNum();
//            log.info("cell count is ..."+cellCount);
//            for (int j=0;j<cellCount;j++){
//                log.info(sheet.getRow(column).getCell(j).getStringCellValue());
//            }


//            visit("https://www.food.com/recipe/"+value);
//            waitForVisibility(FoodHeaderName,30);
//            Assert.assertTrue(true);
//            headerValue();
//            readyInMin();
//            getDirectionList();
//            getIntegrationList();
//            getNutritionValue();

        } catch (Exception e) {
            e.printStackTrace();
            log.error("failed due to :::" + e.getMessage());
            Assert.fail(e.getMessage());
        }
    }


    public String headerValue() {

        try {
            waitForVisibility(FoodHeaderName, 30);
            Assert.assertTrue(true);
            strFoodHeader = FoodHeaderName.getText();
            log.info("the header value is " + strFoodHeader);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("failed due to :::" + e.getMessage());
            Assert.fail(e.getMessage());
        }
        return strFoodHeader;
    }

    public String readyInMin() {

        try {
            waitForVisibility(readyMin, 30);
            Assert.assertTrue(true);
            strReadyInMin = readyMin.getText();
            log.info("The ready in Mins value is " + strReadyInMin);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("failed due to :::" + e.getMessage());
            Assert.fail(e.getMessage());
        }
        return strReadyInMin;
    }

    public String getDirectionList() {

        try {
            waitForVisibility(directionList, 30);
            Assert.assertTrue(true);
            strDirectionList = directionList.getText();
            log.info("The Direction list value is " + strDirectionList);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("failed due to :::" + e.getMessage());
            Assert.fail(e.getMessage());
        }
        return strDirectionList;
    }

    public String getIntegrationList() {

        try {
            waitForVisibility(integrationList, 30);
            Assert.assertTrue(true);
            strIntegrationList = integrationList.getText();
            log.info("The integration list value is " + strIntegrationList);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("failed due to :::" + e.getMessage());
            Assert.fail(e.getMessage());
        }
        return strIntegrationList;
    }

    public String getNutritionValue() {

        try {
            nutritionInfoLink.click();
            waitForVisibility(nutritionPopUpInfo, 30);
            Assert.assertTrue(true);
            strNutritionInfo = nutritionPopUpInfo.getText();
            log.info("The Nutrition list value is " + strNutritionInfo);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("failed due to :::" + e.getMessage());
            Assert.fail(e.getMessage());
        }
        return strNutritionInfo;
    }

    public void excelData() {

        try {
            File excelFile = new File("src/test/resources/FOODDATA.xlsx");
            FileInputStream fis = new FileInputStream(excelFile);

            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            XSSFSheet sheet = workbook.getSheetAt(0);

            Iterator<Row> rowIt = sheet.iterator();

            while (rowIt.hasNext()) {
                Row row = rowIt.next();

                Iterator<Cell> cellIt = row.cellIterator();

                while (cellIt.hasNext()) {
                    Cell cell = cellIt.next();

                    System.out.print(cell.getStringCellValue() + " ");
                }

                System.out.println();
            }

            workbook.close();
            fis.close();
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }


    }


}

