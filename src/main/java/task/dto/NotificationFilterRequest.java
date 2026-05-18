package task.dto;

import task.model.Notification;

import java.util.List;

public record NotificationFilterRequest(List<Notification> notifications, Long senderId) {

}
