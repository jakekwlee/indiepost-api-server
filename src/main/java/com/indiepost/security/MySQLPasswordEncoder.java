package com.indiepost.security;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.UnsupportedEncodingException;

/**
 * Created by jake on 7/26/16.
 */

public class MySQLPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
        try {
            byte[] utf8Bytes = rawPassword.toString().getBytes("UTF-8");
            return "*" + DigestUtils.sha1Hex(DigestUtils.sha1(utf8Bytes)).toUpperCase();
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return this.encode(rawPassword).equals(encodedPassword);
    }
}
