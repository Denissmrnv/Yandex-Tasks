package task.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import task.dto.NotificationFilterRequest;
import task.external.NotificationHistoryProvider;
import task.external.UserPreferenceProvider;
import task.model.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationHistoryProvider notificationHistoryProvider;

    @Mock
    private UserPreferenceProvider userPreferenceProvider;

    @InjectMocks
    private NotificationService notificationService;

    private Notification notification;

    @BeforeEach
    void setUp() {
        notification = new Notification(1L, NotificationType.EMAIL, 100L, "test message");
    }

    @Test
    @DisplayName("Получение отсортированного одного валидного уведомления")
    void givenValidNotification_whenFilterNotification_thenReturnFilteredNotifications() {
        // given
        Long senderId = 10L;
        UserPreference userPreference = new UserPreference(Set.of(NotificationType.EMAIL), Set.of(999L));
        NotificationHistory notificationHistory = new NotificationHistory(new NotificationHistoryItem(List.of(999L), LocalDateTime.now().minusDays(2)));
        NotificationFilterRequest request = new NotificationFilterRequest(List.of(notification), senderId);

        when(userPreferenceProvider.getUserPreference(senderId)).thenReturn(userPreference);
        when(notificationHistoryProvider.getNotificationHistory(senderId)).thenReturn(notificationHistory);

        // when
        List<Notification> result = notificationService.filterNotification(request);

        // then
        assertThat(result).hasSize(1).contains(notification);
    }

    @Test
    @DisplayName("Получение пустого уведомления с учетом блокировки получателя")
    void givenBlockedRecipient_whenFilterNotification_thenReturnEmptyList() {
        // given
        Long senderId = 10L;
        UserPreference userPreference = new UserPreference(Set.of(NotificationType.EMAIL), Set.of(100L));
        NotificationHistory notificationHistory = new NotificationHistory(new NotificationHistoryItem(List.of(),LocalDateTime.now().minusDays(2)));
        NotificationFilterRequest request = new NotificationFilterRequest(List.of(notification), senderId);

        when(userPreferenceProvider.getUserPreference(senderId)).thenReturn(userPreference);
        when(notificationHistoryProvider.getNotificationHistory(senderId)).thenReturn(notificationHistory);

        // when
        List<Notification> result = notificationService.filterNotification(request);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Получение пустого уведомления с учетом несовпадения типа уведомления")
    void givenUnsupportedNotificationType_whenFilterNotification_thenReturnEmptyList() {
        // given
        Long senderId = 10L;
        UserPreference userPreference = new UserPreference(Set.of(NotificationType.PUSH), Set.of());
        NotificationHistory notificationHistory = new NotificationHistory(new NotificationHistoryItem(List.of(999L), LocalDateTime.now().minusDays(2)));
        NotificationFilterRequest request = new NotificationFilterRequest(List.of(notification), senderId);

        when(userPreferenceProvider.getUserPreference(senderId)).thenReturn(userPreference);
        when(notificationHistoryProvider.getNotificationHistory(senderId)).thenReturn(notificationHistory);

        // when
        List<Notification> result = notificationService.filterNotification(request);

        // then
        assertThat(result).isEmpty();
    }


    @Test
    @DisplayName("Получение пустого уведомления с учетом наличия его в истории уведомлений")
    void givenNotificationAlreadyExistsInHistory_whenFilterNotification_thenReturnEmptyList() {
        // given
        Long senderId = 10L;
        UserPreference userPreference = new UserPreference(Set.of(NotificationType.EMAIL), Set.of());
        NotificationHistory notificationHistory = new NotificationHistory(new NotificationHistoryItem(List.of(1L), LocalDateTime.now().minusDays(2)));
        NotificationFilterRequest request = new NotificationFilterRequest(List.of(notification), senderId);

        when(userPreferenceProvider.getUserPreference(senderId)).thenReturn(userPreference);
        when(notificationHistoryProvider.getNotificationHistory(senderId)).thenReturn(notificationHistory);

        // when
        List<Notification> result = notificationService.filterNotification(request);

        // then
        assertThat(result).isEmpty();
    }


    @Test
    @DisplayName("Получение пустого уведомления с учетом недавней даты уведомления")
    void givenRecentNotificationDate_whenFilterNotification_thenReturnEmptyList() {
        // given
        Long senderId = 10L;
        UserPreference userPreference = new UserPreference(Set.of(NotificationType.EMAIL), Set.of());
        NotificationHistory notificationHistory = new NotificationHistory(new NotificationHistoryItem(List.of(), LocalDateTime.now()));
        NotificationFilterRequest request = new NotificationFilterRequest(List.of(notification), senderId);

        when(userPreferenceProvider.getUserPreference(senderId)).thenReturn(userPreference);
        when(notificationHistoryProvider.getNotificationHistory(senderId)).thenReturn(notificationHistory);

        // when
        List<Notification> result = notificationService.filterNotification(request);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Получение пустого уведомления с учетом отсутствия предпочтений")
    void givenNullUserPreference_whenFilterNotification_thenReturnEmptyList() {
        // given
        Long senderId = 10L;
        UserPreference userPreference = new UserPreference(Set.of(NotificationType.EMAIL), Set.of());
        NotificationHistory notificationHistory = new NotificationHistory(new NotificationHistoryItem(List.of(), LocalDateTime.now()));
        NotificationFilterRequest request = new NotificationFilterRequest(List.of(notification), senderId);

        when(userPreferenceProvider.getUserPreference(senderId)).thenReturn(null);
        when(notificationHistoryProvider.getNotificationHistory(senderId)).thenReturn(notificationHistory);

        // when
        List<Notification> result = notificationService.filterNotification(request);

        // then
        assertThat(result).isEmpty();
    }

}