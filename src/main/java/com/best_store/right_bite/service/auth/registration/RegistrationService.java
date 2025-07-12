package com.best_store.right_bite.service.auth.registration;

import com.best_store.right_bite.dto.auth.registration.RegistrationCredentialsDto;
import jakarta.validation.Valid;
import org.springframework.lang.NonNull;

public interface RegistrationService {
    void registration(@NonNull @Valid RegistrationCredentialsDto credentials);
}
