package com.example.pjaidmobile.report;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiSelector;

import com.example.pjaidmobile.R;
import com.example.pjaidmobile.presentation.common.MainActivity;
import com.example.pjaidmobile.presentation.features.report.CreateTicketActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class CreateTicketActivityTest {


    @Rule
    public ActivityScenarioRule<CreateTicketActivity> activityRule =
            new ActivityScenarioRule<>(CreateTicketActivity.class);

    @Before
    public void grantLocationPermissionIfNeeded() throws Exception {
        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());


        UiObject allowButton = device.findObject(new UiSelector()
                .clickable(true)
                .checkable(false)
                .textMatches("(?i)(Only this time|zezwól tylko tym razem|tylko tym razem|allow|zezwól)"));


        if (allowButton.waitForExists(5000)) {
            allowButton.click();
        }
    }

    @Test
    public void whenActivityLaunched_thenFormViewsShouldBeVisible() {
        onView(withId(R.id.editTextTitle)).check(matches(isDisplayed()));
        onView(withId(R.id.editTextDescription)).check(matches(isDisplayed()));
        onView(withId(R.id.buttonSubmitTicket)).check(matches(isDisplayed()));
    }

    @Test
    public void givenFilledForm_whenSubmitClicked_thenShouldReturnToMainActivity() {
        ActivityScenario<MainActivity> mainScenario = ActivityScenario.launch(MainActivity.class);


        onView(withId(R.id.buttonReportIssue)).perform(click());


        onView(withId(R.id.editTextTitle)).perform(replaceText("Brak zasilania"), closeSoftKeyboard());
        onView(withId(R.id.editTextDescription)).perform(replaceText("Nie działa oświetlenie w sali 105."), closeSoftKeyboard());

        onView(withId(R.id.buttonSubmitTicket)).perform(click());


        onView(withId(R.id.buttonScanQR)).check(matches(isDisplayed()));
        mainScenario.close();
    }

    @Test
    public void givenEmptyTitle_whenSubmitClicked_thenShowError() {
        onView(withId(R.id.editTextDescription)).perform(replaceText("Opis"), closeSoftKeyboard());
        onView(withId(R.id.buttonSubmitTicket)).perform(click());

        onView(withId(R.id.editTextTitle)).check(matches(hasErrorText("Tytuł nie może być pusty")));
    }
}
