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
import task.model.Notification;
import task.model.NotificationHistoryItem;
import task.model.NotificationType;

import java.time.LocalDateTime;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    NotificationHistoryProvider notificationHistoryProvider;

    @Mock
    UserPreferenceProvider userPreferenceProvider;

    @InjectMocks
    NotificationService notificationService;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("TEST 1")
    void givenValidNotification_whenAllConditionsPassed_thenReturnTrue() {

        // Given
        Notification notification = new Notification(100L, NotificationType.EMAIL, 123L, "Text email notification");

        NotificationFilterRequest notificationFilterRequest = new NotificationFilterRequest(List.of(notification), 1L);

        NotificationHistoryItem notificationHistoryItem = new NotificationHistoryItem(List.of(100L), LocalDateTime.now());




    }

}