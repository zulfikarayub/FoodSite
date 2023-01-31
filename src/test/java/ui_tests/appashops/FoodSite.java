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
            foodSite_pageObject.ButtonLogOut.click();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("failed due to :::" + e.getMessage());
            Assert.fail(e.getMessage());
        }
    }

    @Test(description = "adding item to the cart")
    public void addingItemToCart() {
        String totalAmount, finalPageAmount;
        int getCount;
        foodSite_pageObject foodSite_pageObject = new foodSite_pageObject(driver);
        try {

            foodSite_pageObject.loginCheck();
            foodSite_pageObject.admitOneLogo.click();
            getCount = foodSite_pageObject.clickOnMujerMenu();
            Assert.assertTrue(getCount > 1);
            foodSite_pageObject.selectItemFromDisplay(3, foodSite_pageObject.availableFirstSizeSelection, foodSite_pageObject.popUpContinueShopping);


            getCount = foodSite_pageObject.clickOnHombreMenu();
            Assert.assertTrue(getCount > 1);
            foodSite_pageObject.selectItemFromDisplay(3, foodSite_pageObject.availableFirstSizeSelection, foodSite_pageObject.popUpContinueShopping);


            foodSite_pageObject.clickOnKidsMenu();
            foodSite_pageObject.selectItemFromDisplay(3, foodSite_pageObject.availableFirstSizeSelection, foodSite_pageObject.popUpGoToCart);

            Thread.sleep(2000);
            foodSite_pageObject.checkOutButton.click();
            log.info("clicking on checkoutButton");
            foodSite_pageObject.continueCustomerDetails.click();
            log.info("clicking on continue customer details");
            foodSite_pageObject.selectDeliveryOption(1);

            foodSite_pageObject.MercadoPaymentMethod(foodSite_pageObject.mercadoPagoPayment);

            totalAmount = foodSite_pageObject.amountSplit(foodSite_pageObject.totalGeneralPayment);

            foodSite_pageObject.finalPayment(foodSite_pageObject.finalPaymentButton);

            Assert.assertTrue(foodSite_pageObject.mercadoPagoPaymentDisplayed.isDisplayed());

            finalPageAmount = foodSite_pageObject.amountSplit(foodSite_pageObject.paymentPageAmount);
            Assert.assertEquals(totalAmount, finalPageAmount);


        } catch (Exception e) {
            e.printStackTrace();
            log.error("failed due to :::" + e.getMessage());
            Assert.fail(e.getMessage());
        }
    }

    @Test(description = "login flow via google account")
    public void lognViaGoogle() {
        foodSite_pageObject foodSite_pageObject = new foodSite_pageObject(driver);
        try {

            visit("https://www.admitone.com.ar/");
            foodSite_pageObject.loginLink.click();
            log.info("clicking on sing in button");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("failed due to :::" + e.getMessage());
            Assert.fail(e.getMessage());
        }
    }
}
