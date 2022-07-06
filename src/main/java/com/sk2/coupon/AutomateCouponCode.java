package com.sk2.coupon;

import java.time.Duration;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static io.github.bonigarcia.wdm.config.DriverManagerType.CHROME;


public class AutomateCouponCode {
    private static final Logger logger = Logger.getLogger(AutomateCouponCode.class.getName());
    private static final String MEMBER_ID = System.getenv().get("MEMBER_ID");

    private static final String[] COUPON_CODES = new String[]
            {"7OUTFITS2", "MONTHOFSEVEN", "WADINGWAITING", "CANUACETHISTEST", "WELCOMEALICE", "LEGENDOFEVAN",
            "HEREWEGOAGAIN", "TOEPASS4YOU", "THEGREATTREASURE", "RAIDGO4IT", "SHINYYELLOW", "SAVEURENERGY",
            "CHECKINHURRY", "EVERY1LIKESRED", "ILOVETHISDRESS", "PONYBOY"};

    public static void main( String[] args ) throws InterruptedException {

        int couponPassed = 0;
        int couponFailed = 0;
        int totalCouponCodes = COUPON_CODES.length;

        WebDriverManager.getInstance(CHROME).setup();

        WebDriver driver = new ChromeDriver();
        driver.get("https://couponview.netmarble.com/coupon/sk2gb/1461");
        System.out.println(driver.getTitle());

        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(2000));

        driver.findElement(By.xpath("//input[@placeholder='Enter member code']")).sendKeys(MEMBER_ID);

        for (String coupon : COUPON_CODES) {
            driver.findElement(By.xpath("//input[@placeholder='Enter coupon code']")).clear();
            driver.findElement(By.xpath("//input[@placeholder='Enter coupon code']")).sendKeys(coupon);

            driver.findElement(By.id("submitCoupon")).click();
            Thread.sleep(500);
            driver.findElement(By.xpath("//div[@id='popReg2']//a[@class='button btn_ok']")).click();
            Thread.sleep(500);
            try {
                driver.findElement(By.xpath("//div[@id='popReg1']//a[@class='button btn_ok']")).click();
                couponFailed += 1;
            } catch (Exception e) {
                driver.findElement(By.xpath("//a[@class='button go_main']")).click();
                Thread.sleep(500);
                driver.findElement(By.xpath("//input[@placeholder='Enter member code']")).clear();
                driver.findElement(By.xpath("//input[@placeholder='Enter member code']")).sendKeys(MEMBER_ID);
                couponPassed += 1;
            }
            Thread.sleep(500);
        }
        driver.quit();

        logger.log(Level.INFO, String.format("%d coupon codes   submitted. %d passed, %d failed.", totalCouponCodes, couponPassed, couponFailed));

    }

}
