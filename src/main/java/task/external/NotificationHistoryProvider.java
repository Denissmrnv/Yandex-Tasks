package task.external;

import task.model.NotificationHistory;

public interface NotificationHistoryProvider {

    NotificationHistory getNotificationHistory(Long recipientId);
}
