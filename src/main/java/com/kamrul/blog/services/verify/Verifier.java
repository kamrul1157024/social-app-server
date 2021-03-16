package com.kamrul.blog.services.verify;

import com.kamrul.blog.configuration.Verifiable;
import com.kamrul.blog.services.verify.exception.VerificationException;

public interface Verifier<T extends Verifiable> {

     void verify (T content) throws VerificationException;

}
