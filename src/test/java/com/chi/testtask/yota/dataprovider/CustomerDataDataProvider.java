package com.chi.testtask.yota.dataprovider;

import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.DataProvider;

public interface CustomerDataDataProvider {

    String customerID = "33550662-0b96-41c7-b7c6-478eea754935";
    String RandomCustomerName = "Name" + RandomStringUtils.randomAlphabetic(5);
    String RandomAdditionString = RandomStringUtils.randomAlphabetic(7);
    long RandomPhone = 79289024530L;



    @DataProvider(name = "PostCustomerDataProvider")
    static Object[][] PostCustomerData() {
        return new Object[][]{{RandomPhone, RandomCustomerName, RandomAdditionString}};
    }

    @DataProvider(name = "GetCustomerByIDDataProvider")
    static Object[][] GetCustomerData() {
        return new Object[][]{{RandomPhone, RandomCustomerName}};
    }

    @DataProvider(name = "CustomerStatusDataProvider")
    static Object[][] GetCustomerStatus() {
        return new Object[][]{{"NewStatus"}, {"InactiveStatus"}};
    }
    @DataProvider(name = "InvalidCustomerDataProvider")
    static Object[][] InvalidCustomerData() {
        return new Object[][]{{12, RandomCustomerName, ""},{RandomPhone,"",RandomAdditionString},{0,"dsf sdf sdf 222",RandomAdditionString}};
    }
}
