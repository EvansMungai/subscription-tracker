package com.subscriptiontracker.subscription_tracker.service;

import com.subscriptiontracker.subscription_tracker.model.Subscription;
import com.subscriptiontracker.subscription_tracker.model.SubscriptionStatus;
import com.subscriptiontracker.subscription_tracker.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SubscriptionService {
    
    @Autowired
    private SubscriptionRepository subscriptionRepository;
    
    public List<Subscription> getAllSubscriptions() {
        return subscriptionRepository.findAll();
    }
    
    public List<Subscription> getActiveSubscriptions() {
        return subscriptionRepository.findByStatus(SubscriptionStatus.ACTIVE);
    }
    
    public Subscription saveSubscription(Subscription subscription) {
        return subscriptionRepository.save(subscription);
    }
    
    public void deleteSubscription(Long id) {
        subscriptionRepository.deleteById(id);
    }
    
    public Subscription getSubscriptionById(Long id) {
        return subscriptionRepository.findById(id).orElse(null);
    }
    
    // Dashboard Statistics
    public long getActiveSubscriptionCount() {
        return subscriptionRepository.countByStatus(SubscriptionStatus.ACTIVE);
    }
    
    public BigDecimal getMonthlyTotal() {
        return getActiveSubscriptions().stream()
                .map(Subscription::getMonthlyPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    public BigDecimal getYearlyTotal() {
        return getMonthlyTotal().multiply(BigDecimal.valueOf(12));
    }
    
    public List<Subscription> getUpcomingRenewals() {
        return getActiveSubscriptions().stream()
                .filter(sub -> sub.getDaysUntilRenewal() <= 7 && sub.getDaysUntilRenewal() >= 0)
                .sorted((a, b) -> a.getNextRenewalDate().compareTo(b.getNextRenewalDate()))
                .collect(Collectors.toList());
    }
    
    public long getRenewingThisWeekCount() {
        return getUpcomingRenewals().size();
    }
    
    public Map<String, BigDecimal> getSpendingByCategory() {
        return getActiveSubscriptions().stream()
                .collect(Collectors.groupingBy(
                    Subscription::getCategory,
                    Collectors.reducing(BigDecimal.ZERO, 
                                      Subscription::getMonthlyPrice, 
                                      BigDecimal::add)
                ));
    }
    
    // Initialize sample data
    public void initializeSampleData() {
        if (subscriptionRepository.count() == 0) {
            List<Subscription> sampleSubs = List.of(
                createSubscription("Netflix Premium", "Streaming", "15.49", 
                                 LocalDate.now().plusDays(18), "fab fa-netflix-n", "netflix"),
                createSubscription("Spotify Premium", "Music Streaming", "9.99", 
                                 LocalDate.now().plusDays(5), "fab fa-spotify", "spotify"),
                createSubscription("Adobe Creative Cloud", "Design Software", "52.99", 
                                 LocalDate.now().plusDays(31), "fab fa-adobe", "adobe"),
                createSubscription("Microsoft 365", "Productivity Suite", "6.99", 
                                 LocalDate.now().plusDays(45), "fab fa-microsoft", "office"),
                createSubscription("Dropbox Plus", "Cloud Storage", "11.99", 
                                 LocalDate.now().plusDays(8), "fab fa-dropbox", "dropbox"),
                createSubscription("Amazon Prime", "Shopping & Streaming", "14.98", 
                                 LocalDate.now().plusDays(83), "fab fa-amazon", "amazon"),
                createSubscription("GitHub Pro", "Development Platform", "4.00", 
                                 LocalDate.now(), "fab fa-github", "github"),
                createSubscription("Notion Pro", "Note Taking & Planning", "8.00", 
                                 LocalDate.now().plusDays(37), "fas fa-sticky-note", "notion")
            );
            
            subscriptionRepository.saveAll(sampleSubs);
        }
    }
    
    private Subscription createSubscription(String name, String category, String price, 
                                          LocalDate renewalDate, String icon, String colorClass) {
        Subscription sub = new Subscription();
        sub.setServiceName(name);
        sub.setCategory(category);
        sub.setMonthlyPrice(new BigDecimal(price));
        sub.setNextRenewalDate(renewalDate);
        sub.setStatus(SubscriptionStatus.ACTIVE);
        sub.setIconClass(icon);
        sub.setColorClass(colorClass);
        return sub;
    }
}
