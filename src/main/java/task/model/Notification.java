package task.model;

public record Notification (Long id, NotificationType notificationType, Long recipientId, String text) {}
