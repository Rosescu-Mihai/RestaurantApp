package com.example.managementrestaurantapp.firebase;

public interface Callback<R> {
    void runResultOnUiThread(R result);
}
