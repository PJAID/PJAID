package com.example.pjaidmobile;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.pjaidmobile.data.model.IssueReport;
import com.example.pjaidmobile.domain.repository.ReportRepository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.observers.TestObserver;

public class SendReportUseCaseTest extends BaseUseCaseTest {

    @Mock
    ReportRepository reportRepository;

    private SendReportUseCase useCase;
    private AutoCloseable closeable;

    @Before
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        useCase = new SendReportUseCase(reportRepository);
    }

    @After
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    public void execute_withValidData_callsRepository() {
        when(reportRepository.sendReport(any(IssueReport.class))).thenReturn(Completable.complete());

        TestObserver<Void> observer = useCase.execute("DEV1", "desc").test();

        observer.assertComplete();
        verify(reportRepository).sendReport(argThat(reportMatches("DEV1", "desc")));
    }

    @Test
    public void execute_withEmptyDescription_returnsError() {
        TestObserver<Void> observer = useCase.execute("DEV1", "").test();

        observer.assertError(IllegalArgumentException.class);
        verify(reportRepository, never()).sendReport(any());
    }

    @Test
    public void testExecute_nullDescription_shouldReturnError() {
        useCase.execute("device123", null)
                .test()
                .assertError(IllegalArgumentException.class);
    }

    @Test
    public void testExecute_emptyDescription_shouldReturnError() {
        useCase.execute("device123", "   ")
                .test()
                .assertError(IllegalArgumentException.class);
    }

    @Test
    public void testExecute_repositoryFails_shouldPropagateError() {
        RuntimeException exception = new RuntimeException("Server error");
        when(reportRepository.sendReport(any()))
                .thenReturn(Completable.error(exception));

        useCase.execute("device123", "opis")
                .test()
                .assertError(throwable ->
                        throwable instanceof RuntimeException &&
                                "Server error".equals(throwable.getMessage())
                );

        verify(reportRepository).sendReport(any());
    }


    private ArgumentMatcher<IssueReport> reportMatches(String deviceId, String description) {
        return report -> deviceId.equals(report.getDeviceId()) && description.equals(report.getDescription());
    }
}