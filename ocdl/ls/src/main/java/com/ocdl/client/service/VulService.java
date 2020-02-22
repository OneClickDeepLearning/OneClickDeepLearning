package com.ocdl.client.service;

import com.ocdl.client.dto.VulDto;
import com.ocdl.client.util.Response;

public interface VulService {

    Response predict(VulDto vulDto);
}
