package com.deathbycaptcha;


public class InvalidCaptchaException extends Exception
{
    public InvalidCaptchaException(String message)
    {
        super(message);
    }
}
