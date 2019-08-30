package com.mb.service;

public interface UserService {
	
	void increasePoints(final long forUserId, final long by);
	long getPoints(final long forUserId);
}
