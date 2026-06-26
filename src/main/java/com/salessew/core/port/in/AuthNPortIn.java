package com.salessew.core.port.in;

import com.salessew.adapter.in.web.dto.LoginRequestDTO;
import com.salessew.adapter.in.web.dto.LoginResponseDTO;

public interface AuthNPortIn {

    LoginResponseDTO execute(LoginRequestDTO req);
}
