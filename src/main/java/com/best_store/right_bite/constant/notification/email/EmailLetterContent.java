package com.best_store.right_bite.constant.notification.email;

import lombok.experimental.UtilityClass;

@UtilityClass
public class EmailLetterContent {
    public final String DEFAULT_NOTIFICATION_SUBJECT = "Information notification";
    public final String GREETING_SUBJECT = "Greeting";
    public final String GREETING = """
            <h2>🎉 Congratulations!</h2>
                <p>Welcome to our store! We're glad to have you on board.</p>
                <p>Start exploring the best deals and exclusive offers now:</p>
                <a href="https://t3vel.github.io/online-store-frontend/" class="button">Visit Our Store</a>
            """;


    public final String RESET_PASSWORD_SUBJECT = "Reset Password";
    public final String RESEt_PASSWORD_CONTENT = """
              <h2>Password Reset Confirmation</h2>
                <p>Dear %s,</p>
                <p>Your password has been successfully reset. Please use the following password to log in to your account: %s</p>
            
                <p>We recommend changing this password after you log in.</p>
                <p>If you did not request this change, please contact our support team immediately.</p>
            
                <a href="https://t3vel.github.io/online-store-frontend/" class="button">Visit Our Store</a>
            
            """;
}
