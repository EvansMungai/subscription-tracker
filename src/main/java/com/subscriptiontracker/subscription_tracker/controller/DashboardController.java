package com.subscriptiontracker.subscription_tracker.controller;

import com.subscriptiontracker.subscription_tracker.model.Subscription;
import com.subscriptiontracker.subscription_tracker.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RestController
@RequestMapping("/api/subscriptions")
public class DashboardController {

    @Autowired
    private SubscriptionService subscriptionService;

    @GetMapping
    public List<Subscription> getAllSubscriptions() {
        return subscriptionService.getAllSubscriptions();
    }

    @GetMapping("/active")
    public List<Subscription> getActiveSubscriptions() {
        return subscriptionService.getActiveSubscriptions();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Subscription> getSubscriptionById(@PathVariable Long id) {
        Subscription subscription = subscriptionService.getSubscriptionById(id);
        if (subscription != null) {
            return ResponseEntity.ok(subscription);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public Subscription createSubscription(@RequestBody Subscription subscription) {
        return subscriptionService.saveSubscription(subscription);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Subscription> updateSubscription(@PathVariable Long id, @RequestBody Subscription subscriptionDetails) {
        Subscription subscription = subscriptionService.getSubscriptionById(id);
        if (subscription != null) {
            subscription.setServiceName(subscriptionDetails.getServiceName());
            subscription.setCategory(subscriptionDetails.getCategory());
            subscription.setMonthlyPrice(subscriptionDetails.getMonthlyPrice());
            subscription.setNextRenewalDate(subscriptionDetails.getNextRenewalDate());
            subscription.setStatus(subscriptionDetails.getStatus());
            subscription.setDescription(subscriptionDetails.getDescription());
            subscription.setIconClass(subscriptionDetails.getIconClass());
            subscription.setColorClass(subscriptionDetails.getColorClass());
            
            final Subscription updatedSubscription = subscriptionService.saveSubscription(subscription);
            return ResponseEntity.ok(updatedSubscription);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSubscription(@PathVariable Long id) {
        subscriptionService.deleteSubscription(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/upcoming-renewals")
    public List<Subscription> getUpcomingRenewals() {
        return subscriptionService.getUpcomingRenewals();
    }
}

