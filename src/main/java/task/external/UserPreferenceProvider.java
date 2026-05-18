package task.external;

import task.model.UserPreference;

public interface UserPreferenceProvider {
    UserPreference getUserPreference(Long recipientId);
}
