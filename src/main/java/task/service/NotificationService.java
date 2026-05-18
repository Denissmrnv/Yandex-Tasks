package task.service;

import task.dto.NotificationFilterRequest;
import task.external.NotificationHistoryProvider;
import task.external.UserPreferenceProvider;
import task.model.Notification;
import task.model.NotificationHistory;
import task.model.NotificationHistoryItem;
import task.model.UserPreference;

import java.time.LocalDateTime;
import java.util.List;

public class NotificationService {

    private final NotificationHistoryProvider notificationHistoryProvider;
    private final UserPreferenceProvider userPreferenceProvider;

    NotificationService(NotificationHistoryProvider notificationHistoryProvider, UserPreferenceProvider userPreferenceProvider) {
        this.notificationHistoryProvider = notificationHistoryProvider;
        this.userPreferenceProvider = userPreferenceProvider;
    }

    public List<Notification> filterNotification(NotificationFilterRequest request) {

        NotificationHistory notificationHistory = notificationHistoryProvider.getNotificationHistory(request.senderId());
        UserPreference userPreference = userPreferenceProvider.getUserPreference(request.senderId());

        NotificationHistoryItem notificationHistoryItem = notificationHistory.notificationHistoryItem();

        return request.notifications().stream()
                .filter(n -> !userPreference.blockedUsersIds().contains(n.recipientId()))
                .filter(n -> userPreference.types().contains(n.notificationType()))
                .filter(n -> !notificationHistoryItem.notificationsIds().contains(n.id()))
                .filter(n -> !notificationHistoryItem.notificationDate().isAfter(LocalDateTime.now().minusDays(1)))
                .toList();

    }

}
