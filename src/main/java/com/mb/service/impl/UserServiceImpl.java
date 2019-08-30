package com.mb.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.mb.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	final Map<Long, Long> usersWithPoints = new HashMap<>();

	@Override
	public void increasePoints(final long forUserId, final long by) {
		long points = by;
		
		if (usersWithPoints.containsKey(forUserId)) {
			points += usersWithPoints.get(forUserId);
		}
		
		usersWithPoints.put(forUserId, points);
	}

	@Override
	public long getPoints(final long forUserId) {
		if (!usersWithPoints.containsKey(forUserId)) {
			return 0;
		}

		return usersWithPoints.get(forUserId);
	}

}
