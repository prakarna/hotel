package com.exercise.common.ratelimit;

import java.util.Date;
import java.util.LinkedList;

public class RateLimit {
	//linkedlist will keep time entries to keep track of the numberOfCalls per timeWindow
	private LinkedList<Long> bucket = new LinkedList<Long>();
	private long suspendedStart = 0L;
	 
	//default value, by default each client id can call only 1 request per 10 second. If exceed the numberOfCalls, it will be suspended for 30 second
    private int numberOfCalls=1;
    private long timeWindow=10000;
    private long suspendedTime=30000;
    private boolean suspendedFlag = false;
    
    public RateLimit(int numberOfCalls, long timeWindow, long suspendedTime) {
    	this.numberOfCalls = numberOfCalls;
        this.timeWindow = timeWindow;
        this.suspendedTime = suspendedTime;
    }

    public synchronized boolean isExceeded() {
        long now = new Date().getTime();
        long validTime = now - this.timeWindow;
        long longestEntry = 0L;
        while (!this.bucket.isEmpty()) {
        	longestEntry = this.bucket.getLast().longValue();
        	if (longestEntry <= validTime) {
        		this.bucket.removeLast();
        	} else {
        		break;
        	}
        }
        if (this.bucket.size() >= this.numberOfCalls) {
        	suspendedFlag = true;
        	suspendedStart = new Date().getTime();
        	return true;
        }
         
        this.bucket.addFirst(new Long(now));
        return false;
    }
     
    public synchronized boolean isSuspended() {
    	long now = new Date().getTime();
    	long suspendedPeriod = now - suspendedStart;
    	if (suspendedPeriod <= suspendedTime) {
    		return true;
    	}
    	suspendedFlag = false;
    	return false;
    }

	public boolean getSuspendedFlag() {
		return suspendedFlag;
	}

	public void setSuspendedFlag(boolean suspendedFlag) {
		this.suspendedFlag = suspendedFlag;
	}

} 
