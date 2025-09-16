package com.best_store.right_bite.constant.notification.holder.letter;

import lombok.experimental.UtilityClass;

@UtilityClass
public class GreetingVariablesHolder {
    public final String EMAIL_SUBJECT = "Welcome!";
    public final String BODY_TITLE = "🎉 Congratulations!";
    public final String GREETING = """
                    <p style="margin: 0 0 15px 0; font-size: 16px; color: #333333;">
                        Welcome to our store! We're glad to have you on board!</p>
                    <p style="margin: 0 0 15px 0; font-size: 16px; color: #333333;">
                        Start exploring the best deals and exclusive offers now:</p>
                    <p style="margin: 0 0 20px 0; font-size: 16px; color: #333333;"></p>
                    <a href="https://t3vel.github.io/online-store-frontend/"
                       style="display: inline-block;
                              background-color: #28a745;
                              color: #ffffff;
                              padding: 12px 24px;
                              text-decoration: none;
                              border-radius: 6px;
                              font-weight: bold;
                              font-size: 16px;
                              box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);">
                        Visit Our Store
                    </a>
            """;
}
