package com.mb.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.mb.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	private final Map<Long, Long> usersWithPoints = new HashMap<>();

	@Override
	public synchronized void increasePoints(final long forUserId, final long by) {
		usersWithPoints.compute(forUserId, (key, val) -> (val == null) ? by : val + by);
	}

	@Override
	public synchronized long getPoints(final long forUserId) {
		return usersWithPoints.getOrDefault(forUserId, 0L);
	}

}
