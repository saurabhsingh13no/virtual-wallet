package com.upgrade.infinitispace.virtualwallet.repository;

import com.upgrade.infinitispace.virtualwallet.models.Wallet;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface WalletRepository extends CrudRepository<Wallet, Integer> {
    @Query("SELECT w FROM Wallet  w WHERE w.walletId=:walletId")
    Iterable<Wallet> findWalletById(@Param("walletId") Integer walletId);
}
