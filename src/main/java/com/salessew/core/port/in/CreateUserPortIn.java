package com.salessew.core.port.in;

import com.salessew.core.domain.User;

public interface CreateUserPortIn {

    User execute(User req);
}
