package com.example.pjaidmobile;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.pjaidmobile.data.model.Device;
import com.example.pjaidmobile.domain.repository.DeviceRepository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.observers.TestObserver;

public class GetDeviceByIdUseCaseTest extends BaseUseCaseTest {

    @Mock
    DeviceRepository repository;

    private GetDeviceByIdUseCase useCase;
    private AutoCloseable closeable;


    @Before
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        useCase = new GetDeviceByIdUseCase(repository);
    }

    @After
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    public void execute_withValidId_returnsDevice() {
        Device device = new Device();
        when(repository.getDeviceById("123")).thenReturn(Single.just(device));

        TestObserver<Device> observer = useCase.execute("123").test();

        observer.assertComplete();
        observer.assertValue(device);
        verify(repository).getDeviceById("123");
    }

    @Test
    public void execute_withNonExistingId_returnsError() {
        when(repository.getDeviceById("xyz"))
                .thenReturn(Single.error(new RuntimeException("Not found")));

        TestObserver<Device> observer = useCase.execute("xyz").test();

        observer.assertError(throwable ->
                throwable instanceof RuntimeException &&
                        "Not found".equals(throwable.getMessage())
        );

        verify(repository).getDeviceById("xyz");
    }

    @Test
    public void execute_withNullId_returnsError() {
        TestObserver<Device> observer = useCase.execute(null).test();

        observer.assertError(IllegalArgumentException.class);
        verify(repository, never()).getDeviceById(anyString());
    }


    @Test
    public void execute_withEmptyId_returnsError() {
        TestObserver<Device> observer = useCase.execute("").test();

        observer.assertError(IllegalArgumentException.class);
        verify(repository, never()).getDeviceById(anyString());
    }
}