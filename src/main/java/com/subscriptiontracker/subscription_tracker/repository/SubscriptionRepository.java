package com.subscriptiontracker.subscription_tracker.repository;

import com.subscriptiontracker.subscription_tracker.model.Subscription;
import com.subscriptiontracker.subcription_tracker.model.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    
    List<Subscription> findByStatus(SubscriptionStatus status);
    
    long countByStatus(SubscriptionStatus status);
    
    List<Subscription> findByCategory(String category);
    
    @Query("SELECT s FROM Subscription s WHERE s.nextRenewalDate BETWEEN ?1 AND ?2")
    List<Subscription> findByNextRenewalDateBetween(LocalDate startDate, LocalDate endDate);
    
    @Query("SELECT s FROM Subscription s WHERE s.nextRenewalDate <= ?1 AND s.status = ?2")
    List<Subscription> findUpcomingRenewals(LocalDate date, SubscriptionStatus status);
    
    List<Subscription> findByServiceNameContainingIgnoreCase(String serviceName);
}
