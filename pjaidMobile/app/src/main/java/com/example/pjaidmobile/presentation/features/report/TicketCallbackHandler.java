package com.example.pjaidmobile.presentation.features.report;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.example.pjaidmobile.data.model.TicketResponse;

import retrofit2.Response;

public class TicketCallbackHandler {

    private final Context context;

    public TicketCallbackHandler(Context context) {
        this.context = context;
    }

    public void onSuccess(TicketResponse ticketResponse) {
        Toast.makeText(context,
                "Zgłoszenie dodane prawidłowo!\nID: " + ticketResponse.getId() +
                        "\nStatus: " + ticketResponse.getStatus(),
                Toast.LENGTH_LONG).show();
        ((Activity) context).finish();
    }

    public void onError(Response<?> response) {
        String errorMsg = getErrorMessage(response.code());
        Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show();
    }

    public void onFailure(Throwable t) {
        String errorMsg = resolveErrorMessage(t);
        Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show();
    }

    private String resolveErrorMessage(Throwable t) {
        if (t.getMessage() == null) return "Błąd przy dodawaniu zgłoszenia";
        if (t.getMessage().contains("timeout")) return "Przekroczono czas oczekiwania - sprawdź połączenie";
        if (t.getMessage().contains("Unable to resolve host")) return "Brak połączenia z internetem";
        return "Błąd połączenia: " + t.getMessage();
    }

    private String getErrorMessage(int errorCode) {
        switch (errorCode) {
            case 400: return "Nieprawidłowe dane zgłoszenia";
            case 401: return "Brak autoryzacji";
            case 403: return "Brak uprawnień";
            case 500: return "Błąd serwera - spróbuj ponownie";
            default:  return "Błąd: " + errorCode;
        }
    }
}
