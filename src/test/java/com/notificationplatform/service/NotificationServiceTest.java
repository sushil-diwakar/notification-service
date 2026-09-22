package com.notificationplatform.service;

import com.notificationplatform.dto.CreateNotificationRequest;
import com.notificationplatform.dto.NotificationResponse;
import com.notificationplatform.entity.Notification;
import com.notificationplatform.entity.NotificationChannel;
import com.notificationplatform.entity.NotificationStatus;
import com.notificationplatform.exception.NotificationNotFoundException;
import com.notificationplatform.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link NotificationService} using Mockito.
 *
 * <p>Here we test the business logic in pure isolation without loading
 * the Spring ApplicationContext, ensuring fast and deterministic test runs.
 */
@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        notificationService = new NotificationService(notificationRepository);
    }

    @Test
    @DisplayName("createNotification assigns ID, sets status to CREATED, and persists")
    void createNotification_success() {
        CreateNotificationRequest request = new CreateNotificationRequest(
                "user@example.com",
                NotificationChannel.EMAIL,
                "Welcome",
                "Welcome to the platform!"
        );

        when(notificationRepository.save(any(Notification.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        NotificationResponse response = notificationService.createNotification(request);

        assertThat(response).isNotNull();
        assertThat(response.id()).isNotBlank();
        assertThat(response.recipient()).isEqualTo("user@example.com");
        assertThat(response.channel()).isEqualTo(NotificationChannel.EMAIL);
        assertThat(response.subject()).isEqualTo("Welcome");
        assertThat(response.message()).isEqualTo("Welcome to the platform!");
        assertThat(response.status()).isEqualTo(NotificationStatus.CREATED);
        assertThat(response.createdAt()).isNotNull();
        assertThat(response.updatedAt()).isNotNull();

        ArgumentCaptor<Notification> captor = ArgumentCaptor.forClass(Notification.class);
        verify(notificationRepository, times(1)).save(captor.capture());

        Notification saved = captor.getValue();
        assertThat(saved.getId()).isEqualTo(response.id());
        assertThat(saved.getStatus()).isEqualTo(NotificationStatus.CREATED);
    }

    @Test
    @DisplayName("getNotificationById returns notification when found")
    void getNotificationById_existingId_returnsNotification() {
        String id = "test-id-123";
        Instant now = Instant.now();
        Notification notification = new Notification(
                id,
                "+1234567890",
                NotificationChannel.SMS,
                null,
                "Your OTP is 123456",
                NotificationStatus.CREATED,
                now,
                now
        );

        when(notificationRepository.findById(id)).thenReturn(Optional.of(notification));

        NotificationResponse response = notificationService.getNotificationById(id);

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(id);
        assertThat(response.recipient()).isEqualTo("+1234567890");
        assertThat(response.channel()).isEqualTo(NotificationChannel.SMS);
        assertThat(response.message()).isEqualTo("Your OTP is 123456");
        assertThat(response.status()).isEqualTo(NotificationStatus.CREATED);
    }

    @Test
    @DisplayName("getNotificationById throws NotificationNotFoundException when ID does not exist")
    void getNotificationById_nonExistingId_throwsException() {
        String id = "non-existing-id";
        when(notificationRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> notificationService.getNotificationById(id))
                .isInstanceOf(NotificationNotFoundException.class)
                .hasMessageContaining("Notification not found with id: " + id);
    }

    @Test
    @DisplayName("getAllNotifications returns all saved notifications")
    void getAllNotifications_returnsList() {
        Instant now = Instant.now();
        Notification n1 = new Notification("id-1", "user1@example.com", NotificationChannel.EMAIL, "Sub1", "Msg1", NotificationStatus.CREATED, now, now);
        Notification n2 = new Notification("id-2", "user2@example.com", NotificationChannel.PUSH, null, "Msg2", NotificationStatus.SENT, now, now);

        when(notificationRepository.findAll()).thenReturn(List.of(n1, n2));

        List<NotificationResponse> result = notificationService.getAllNotifications();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).id()).isEqualTo("id-1");
        assertThat(result.get(1).id()).isEqualTo("id-2");
    }
}
