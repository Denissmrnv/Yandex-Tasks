package task.model;


import java.util.Set;

public record UserPreference(Set<NotificationType> types, Set<Long> blockedUsersIds) {
}
