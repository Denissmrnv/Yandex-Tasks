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
import java.util.Optional;

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
                .filter(n ->
                        Optional.ofNullable(userPreference)
                                .map(UserPreference::blockedUsersIds)
                                .map(ids -> !ids.contains(n.recipientId()))
                                .orElse(false))
                .filter(n ->
                        Optional.ofNullable(userPreference)
                                .map(UserPreference::types)
                                .map(types -> types.contains(n.notificationType()))
                                .orElse(false))
                .filter(n ->
                        Optional.ofNullable(notificationHistoryItem)
                                .map(NotificationHistoryItem::notificationsIds)
                                .map(ids -> !ids.contains(n.id()))
                                .orElse(false))
                .filter(n ->
                        Optional.ofNullable(notificationHistoryItem)
                                .map(NotificationHistoryItem::notificationDate)
                                .map(date -> !date.isAfter(LocalDateTime.now().minusDays(1)))
                                .orElse(false)).toList();
    }


}
