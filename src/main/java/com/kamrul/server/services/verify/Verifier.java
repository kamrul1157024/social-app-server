package com.kamrul.server.services.verify;

import com.kamrul.server.configuration.Verifiable;
import com.kamrul.server.services.verify.exception.VerificationException;

public interface Verifier<T extends Verifiable> {

     void verify (T content) throws VerificationException;

}
