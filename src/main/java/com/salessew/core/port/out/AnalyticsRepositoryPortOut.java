package com.salessew.core.port.out;

import com.salessew.core.domain.Link;

public interface AnalyticsRepositoryPortOut {

    void updateClickCount(Link link);
}
