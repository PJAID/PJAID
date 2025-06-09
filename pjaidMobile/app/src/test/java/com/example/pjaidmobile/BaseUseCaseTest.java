package com.example.pjaidmobile;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import android.util.Log;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.mockito.MockedStatic;

public abstract class BaseUseCaseTest {

    protected static MockedStatic<Log> logMock;

    @BeforeClass
    public static void setUpClass() {
        logMock = mockStatic(Log.class);
        when(Log.d(anyString(), anyString())).thenReturn(0);
    }

    @AfterClass
    public static void tearDownClass() {
        if (logMock != null) {
            logMock.close();
        }
    }
}
