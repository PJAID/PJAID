package com.example.pjaidmobile.data.remote.api.auth;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.pjaidmobile.data.model.AuthResponse;
import com.example.pjaidmobile.data.remote.api.AuthApi;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;

public class TokenAuthenticatorTest {

    @Mock
    Context mockContext;
    @Mock
    SharedPreferences mockPrefs;
    @Mock
    SharedPreferences.Editor mockEditor;
    @Mock
    Retrofit mockRetrofit;
    @Mock
    AuthApi mockAuthApi;
    @Mock
    Call<AuthResponse> mockCall;
    @Mock
    LogoutHandler mockLogoutHandler;

    private TokenAuthenticator authenticator;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(mockContext.getSharedPreferences(anyString(), anyInt())).thenReturn(mockPrefs);
        when(mockPrefs.edit()).thenReturn(mockEditor);
        when(mockEditor.putString(anyString(), anyString())).thenReturn(mockEditor);
        when(mockEditor.clear()).thenReturn(mockEditor);

        authenticator = new TokenAuthenticator(mockContext, mockRetrofit, mockLogoutHandler);
    }

    @Test
    public void authenticate_noRefreshToken_triggersLogout() throws IOException {
        when(mockPrefs.getString(eq("refreshToken"), any())).thenReturn(null);
        Response mockResponse = mock(Response.class);

        Request result = authenticator.authenticate(null, mockResponse);

        verify(mockEditor).clear();
        verify(mockEditor).apply();
        verify(mockLogoutHandler).logout(mockContext);
        assertNull(result);
    }

    @Test
    public void authenticate_successfulRefresh_returnsNewRequest() throws IOException {
        when(mockPrefs.getString(eq("refreshToken"), any())).thenReturn("refresh-token");
        when(mockRetrofit.create(AuthApi.class)).thenReturn(mockAuthApi);
        when(mockAuthApi.refresh(any())).thenReturn(mockCall);

        AuthResponse mockAuthResponse = mock(AuthResponse.class);
        when(mockAuthResponse.getAccessToken()).thenReturn("new-access-token");

        retrofit2.Response<AuthResponse> response = retrofit2.Response.success(mockAuthResponse);
        when(mockCall.execute()).thenReturn(response);

        Request originalRequest = new Request.Builder().url("http://localhost").build();
        Response mockResponse = mock(Response.class);
        when(mockResponse.request()).thenReturn(originalRequest);

        Request result = authenticator.authenticate(null, mockResponse);

        assertNotNull(result);
        assertEquals("Bearer new-access-token", result.header("Authorization"));
        verify(mockEditor).putString("accessToken", "new-access-token");
        verify(mockEditor).apply();
        verify(mockLogoutHandler, never()).logout(any());
    }

    @Test
    public void authenticate_refreshFails_triggersLogout() throws IOException {
        when(mockPrefs.getString(eq("refreshToken"), any())).thenReturn("refresh-token");
        when(mockRetrofit.create(AuthApi.class)).thenReturn(mockAuthApi);
        when(mockAuthApi.refresh(any())).thenReturn(mockCall);

        retrofit2.Response<AuthResponse> response = retrofit2.Response.success(null);
        when(mockCall.execute()).thenReturn(response);

        Response mockResponse = mock(Response.class);
        Request result = authenticator.authenticate(null, mockResponse);

        verify(mockEditor).clear();
        verify(mockEditor).apply();
        verify(mockLogoutHandler).logout(mockContext);
        assertNull(result);
    }

    @Test(expected = IOException.class)
    public void authenticate_refreshThrowsException_propagatesIOException() throws IOException {
        when(mockPrefs.getString(eq("refreshToken"), any())).thenReturn("refresh-token");
        when(mockRetrofit.create(AuthApi.class)).thenReturn(mockAuthApi);
        when(mockAuthApi.refresh(any())).thenReturn(mockCall);
        when(mockCall.execute()).thenThrow(new IOException());

        Response mockResponse = mock(Response.class);
        authenticator.authenticate(null, mockResponse);
    }
}
