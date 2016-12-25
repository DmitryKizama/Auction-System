package com.stkizema.auction;

import com.stkizema.auction.validator.EmailValidator;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EmailValidatorTest {

    @Test
    public void does_not_correct() throws Exception {
        assertEquals(EmailValidator.getInstance().validate("weui"), false);
    }

    @Test
    public void invalid_email() throws Exception {
        assertEquals(EmailValidator.getInstance().validate("weui@"), false);
    }

    @Test
    public void invalid_email_without_dot() throws Exception {
        assertEquals(EmailValidator.getInstance().validate("weui@lsd"), false);
    }

    @Test
    public void invalid_email_without_words_after_dot() throws Exception {
        assertEquals(EmailValidator.getInstance().validate("weui@lsd."), false);
    }

    @Test
    public void valid_email() throws Exception {
        assertEquals(EmailValidator.getInstance().validate("weui@gmail.com"), true);
    }

}
