package com.subscriptiontracker.subscription_tracker.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "subscriptions")
public class Subscription {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String serviceName;
    
    @Column(nullable = false)
    private String category;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal monthlyPrice;
    
    @Column(nullable = false)
    private LocalDate nextRenewalDate;
    
    @Enumerated(EnumType.STRING)
    private SubscriptionStatus status;
    
    private String description;
    private String iconClass;
    private String colorClass;
    
    // Constructors
    public Subscription() {}
    
    public Subscription(String serviceName, String category, BigDecimal monthlyPrice, 
                       LocalDate nextRenewalDate, SubscriptionStatus status) {
        this.serviceName = serviceName;
        this.category = category;
        this.monthlyPrice = monthlyPrice;
        this.nextRenewalDate = nextRenewalDate;
        this.status = status;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getServiceName() {
        return serviceName;
    }
    
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public BigDecimal getMonthlyPrice() {
        return monthlyPrice;
    }
    
    public void setMonthlyPrice(BigDecimal monthlyPrice) {
        this.monthlyPrice = monthlyPrice;
    }
    
    public LocalDate getNextRenewalDate() {
        return nextRenewalDate;
    }
    
    public void setNextRenewalDate(LocalDate nextRenewalDate) {
        this.nextRenewalDate = nextRenewalDate;
    }
    
    public SubscriptionStatus getStatus() {
        return status;
    }
    
    public void setStatus(SubscriptionStatus status) {
        this.status = status;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getIconClass() {
        return iconClass;
    }
    
    public void setIconClass(String iconClass) {
        this.iconClass = iconClass;
    }
    
    public String getColorClass() {
        return colorClass;
    }
    
    public void setColorClass(String colorClass) {
        this.colorClass = colorClass;
    }
    
    // Helper methods
    public BigDecimal getYearlyPrice() {
        return monthlyPrice.multiply(BigDecimal.valueOf(12));
    }
    
    public long getDaysUntilRenewal() {
        return java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), nextRenewalDate);
    }
    
    public boolean isRenewingSoon() {
        return getDaysUntilRenewal() <= 7 && getDaysUntilRenewal() >= 0;
    }
    
    public boolean isDueToday() {
        return getDaysUntilRenewal() == 0;
    }
}

enum SubscriptionStatus {
    ACTIVE,
    PAUSED,
    CANCELLED,
    EXPIRED
}
