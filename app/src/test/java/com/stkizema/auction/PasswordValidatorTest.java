package com.stkizema.auction;

import com.stkizema.auction.validator.PasswordValidator;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class PasswordValidatorTest {
    @Test
    public void small_password() throws Exception {
        assertEquals(PasswordValidator.getInstance().validate("weui"), false);
    }

    @Test
    public void password_without_number() throws Exception {
        assertEquals(PasswordValidator.getInstance().validate("weusdjnfrjsknfrwi"), false);
    }

    @Test
    public void valid_min_pass() throws Exception {
        assertEquals(PasswordValidator.getInstance().validate("qwerty1"), true);
    }

    @Test
    public void invalid_space_password() throws Exception {
        assertEquals(PasswordValidator.getInstance().validate("sa,nfkrwebfbwe r23423fdnsnfcjsedn f43wr4we"), false);
    }

    @Test
    public void valid_password() throws Exception {
        assertEquals(PasswordValidator.getInstance().validate("sanfkrwebfbwr23423fdnsnfcjsednf43wr4we"), true);
    }

}