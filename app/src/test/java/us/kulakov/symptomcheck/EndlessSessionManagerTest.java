package us.kulakov.symptomcheck;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import io.reactivex.Single;
import us.kulakov.symptomcheck.common.TestDataFactory;
import us.kulakov.symptomcheck.data.EndlessSessionManager;
import us.kulakov.symptomcheck.data.local.supporting.SupportingDataProvider;
import us.kulakov.symptomcheck.data.model.response.endless.InitSessionResponse;
import us.kulakov.symptomcheck.data.remote.EndlessService;
import us.kulakov.symptomcheck.util.RxSchedulersOverrideRule;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EndlessSessionManagerTest {

    @Rule
    public final RxSchedulersOverrideRule overrideSchedulersRule = new RxSchedulersOverrideRule();

    @Mock
    private EndlessService service;

    @Mock
    private SupportingDataProvider dataProvider;

    private EndlessSessionManager subject;

    @Before
    public void setUp() {
        subject = new EndlessSessionManager(service, dataProvider);
    }

    @Test
    public void newSession_serviceOk_emitsTrue() {
        InitSessionResponse response = TestDataFactory.makeInitSessionResponse(true);

        when(service.initSession())
                .thenReturn(Single.just(response));

        subject.newSession()
                .test()
                .assertComplete()
                .assertValue(true);
    }
}
