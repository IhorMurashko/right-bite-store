package com.best_store.right_bite.service.auth.registration;

import com.best_store.right_bite.service.notificationService.dispatch.NotificationDispatcherService;
import com.best_store.right_bite.service.user.crud.UserCrudService;
import com.best_store.right_bite.utils.user.UserAssembler;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceImplTest {
    @Mock
    private UserCrudService userCrudService;
    @Mock
    private UserAssembler userAssembler;
    @Mock
    private NotificationDispatcherService notificationDispatcherService;
    @InjectMocks
    private RegistrationServiceImpl registrationService;


}