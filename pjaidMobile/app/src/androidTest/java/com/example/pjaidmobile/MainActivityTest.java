package com.example.pjaidmobile;


import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.pjaidmobile.presentation.common.MainActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        SharedPreferences prefs = context.getSharedPreferences("PJAIDPrefs", Context.MODE_PRIVATE);
        prefs.edit().putBoolean("isLoggedIn", true).apply();
    }

    @Test
    public void checkIfTextViewWithZglosAwariaExists() {
        ActivityScenario.launch(MainActivity.class);
        Espresso.onView(withText("Zgłoś awarię"))
                .check(matches(isDisplayed()));
    }
}
