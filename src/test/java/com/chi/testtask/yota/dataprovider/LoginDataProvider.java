package com.chi.testtask.yota.dataprovider;

import org.testng.annotations.DataProvider;

public interface LoginDataProvider {


    @DataProvider(name = "ValidAdminCredsDataProvider")
    static Object[][] LoginDataAdmin() {
        return new Object[][]{{"admin","password"}};
    }
    @DataProvider(name = "ValidUserCredsDataProvider")
    static Object[][] LoginDataUser() {
        return new Object[][]{{"user","password"}};
    }
    @DataProvider(name = "InValidCredsDataProvider")
    static Object[][] InvalidCreds() {
        return new Object[][]{{"user","bllaBla"},{"admin","dfsdfs"},{"","password"}, {"admin"," "}};
    }

}
