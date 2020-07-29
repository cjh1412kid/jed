package io.nuite.modules.system.model;

import java.util.concurrent.CountDownLatch;

import io.nuite.modules.sr_base.entity.BaseUserEntity;

public class LoginResponse {

	public CountDownLatch latch;

    public BaseUserEntity user;
}
