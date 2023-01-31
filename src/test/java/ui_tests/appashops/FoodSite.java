package ui_tests.appashops;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.foodSite_pageObject;
import utilities.Driver;

public class FoodSite extends Driver {
    public static Logger log = LogManager.getLogger(FoodSite.class);
    public String productName, productPrice;

    @Test(description = "login flow")
    public void loginCheck() {
        foodSite_pageObject foodSite_pageObject = new foodSite_pageObject(driver);
        try {

            foodSite_pageObject.loginCheck();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("failed due to :::" + e.getMessage());
            Assert.fail(e.getMessage());
        }
    }


}
