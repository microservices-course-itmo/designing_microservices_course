package com.microservices.ordermanagement.app.api;

public interface PendingDetailService {

    /**
     * Register new detail in underlying database
     *
     * @param weight the weight of given detail
     * @return the id (barcode) of newly created postponed detail
     */
    int registerPendingDetail(int weight);
}
