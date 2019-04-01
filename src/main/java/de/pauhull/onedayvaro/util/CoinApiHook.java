package de.pauhull.onedayvaro.util;

import cloud.evaped.coinsapi.API.CoinsAPI;

import java.util.UUID;

/**
 * Created by Paul
 * on 22.03.2019
 *
 * @author pauhull
 */
public class CoinApiHook {

    private CoinsAPI coinsApi;

    public CoinApiHook() {
        this.coinsApi = new CoinsAPI();
    }

    public int getCoins(UUID uuid) {
        try {
            return coinsApi.getCoins(uuid.toString());
        } catch (Exception ex) {
            return 0;
        }
    }

}
