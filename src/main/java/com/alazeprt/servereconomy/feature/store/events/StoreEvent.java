package com.alazeprt.servereconomy.feature.store.events;

public interface StoreEvent {
    void onEnable();
    void onDisable();
    void onStop();
    void onRestore();
    void onSell();
    void onBuy();
}
