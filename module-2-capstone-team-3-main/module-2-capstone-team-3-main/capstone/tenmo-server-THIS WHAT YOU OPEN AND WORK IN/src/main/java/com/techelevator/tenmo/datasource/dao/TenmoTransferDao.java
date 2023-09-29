package com.techelevator.tenmo.datasource.dao;

import com.techelevator.tenmo.datasource.model.TenmoTransfer;

import java.util.List;

public interface TenmoTransferDao {

    TenmoTransfer saveTransfer(TenmoTransfer aTransfer);

    List<TenmoTransfer> getTransfersForUser(int userId);

    TenmoTransfer getATransferById(Long transferId);
}
