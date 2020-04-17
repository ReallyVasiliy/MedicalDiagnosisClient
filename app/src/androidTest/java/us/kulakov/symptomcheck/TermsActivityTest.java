package us.kulakov.symptomcheck;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

import io.reactivex.Single;
import us.kulakov.symptomcheck.common.TestComponentRule;
import us.kulakov.symptomcheck.features.terms.TermsActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class TermsActivityTest {

    private final TestComponentRule componentRule =
            new TestComponentRule(InstrumentationRegistry.getTargetContext());
    private final ActivityTestRule<TermsActivity> activityTestRule =
            new ActivityTestRule<>(TermsActivity.class, false, false);

    // TestComponentRule needs to go first to make sure the Dagger ApplicationTestComponent is set
    // in the Application before any Activity is launched.
    @Rule
    public TestRule chain = RuleChain.outerRule(componentRule).around(activityTestRule);

    @Test
    public void clickingBeginOpensTermsDialog() {

        stubNewSession(Single.just(true));
        activityTestRule.launchActivity(null);

        onView(withId(R.id.button_begin)).perform(click());

        onView(withText(R.string.terms_dialog_title))
                .inRoot(isDialog()) // <---
                .check(matches(isDisplayed()));
    }

    public void stubNewSession(Single<Boolean> single) {
        when(componentRule.getMockSessionManager().newSession()).thenReturn(single);
    }

    public void stubAcceptTerms(Single<Boolean> single) {
        when(componentRule.getMockSessionManager().acceptTerms()).thenReturn(single);
    }
}
