package task.model;

import java.time.LocalDateTime;
import java.util.List;

public record NotificationHistoryItem (List<Long> notificationsIds, LocalDateTime notificationDate){
}
