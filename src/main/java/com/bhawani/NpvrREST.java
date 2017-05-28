package com.bhawani;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Logger;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import iptv.npvr.exception.NpvrException;
import iptv.npvr.pojo.Channel;
import iptv.npvr.pojo.NPVR;
import iptv.npvr.pojo.Programme;
import iptv.npvr.pojo.Record;

/**
 * 
 * @author bhawani.singh
 *
 */
@RestController
@RequestMapping(value = "npvr")
public class NpvrREST {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(NpvrREST.class.getName());
	
	static final long ONE_MINUTE_IN_MILLIS = 60000;//millisecs
	
	@RequestMapping(value = "/record",
	method = RequestMethod.POST,
	produces = MediaType.APPLICATION_JSON_VALUE)
	
	@ResponseBody
	//@Produces({MediaType.TEXT_PLAIN})
    public NPVR record(@RequestBody Record record) throws NpvrException {
		LOGGER.info("NpvrREST.record()");
		NPVR npvr = new NPVR();
		npvr.setRecord(record);
		npvr.setStatus("Success");
		LOGGER.info("getChannelId = " + record.getChannelId());
		LOGGER.info("getProgramId = " + record.getProgramId());
		LOGGER.info("getStartTime = " + record.getStartTime());
		
		DateFormat df2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date startTime = null;
		try {
			startTime = df2.parse(record.getStartTime());
			LOGGER.info("Date: " + startTime);
			LOGGER.info("Date in dd-MM-yyyy HH:mm:ss format is: " + df2.format(startTime));			
		} catch (ParseException e) {
			e.printStackTrace();
		}		
		
		Channel channel = null;		
		try {			
			channel = getChannel(record.getChannelId());
					
			if (null != channel) {				
				LOGGER.info("getChannelId = " + channel.getChannelId());
				LOGGER.info("getCallSign = " + channel.getCallSign());
				if(channel.isFallBack()){
					npvr.setStatus("Success: Returned from Circuit Breaker");
					LOGGER.info("Success: Returned from Circuit Breaker");
				} else {
					LOGGER.info("Success");
				}
				npvr.setChannel(channel);				
				Programme programme = getProgramme(record.getProgramId());				
				if (null != programme) {			
					LOGGER.info("Programme get successfully");
					LOGGER.info("getProgId = " + programme.getProgId());
					LOGGER.info("getProgName = " + programme.getProgName());
					LOGGER.info("getProgDuration = " + programme.getProgDuration());
					
					long val = startTime.getTime();
			        
			        Date afterAddingTenMins = new Date(val 
			        		+ ((programme.getProgDuration().intValue()) * ONE_MINUTE_IN_MILLIS));
			        
			        LOGGER.info("Date in dd-MM-yyyy HH:mm:ss format is: " + df2.format(afterAddingTenMins));	
			        
			        record.setEndTime(df2.format(afterAddingTenMins));
					
					npvr.setProgramme(programme);
				} else {
					LOGGER.info("Programme Not found");
					throw new NpvrException("Programme Not found !!",104);				
				}
				
			} else {
				LOGGER.info("Channel Not found");
				throw new NpvrException("Channel Not found !!",103);			
			}			
		} catch (Exception e){
			e.printStackTrace();
		}
		
		return npvr;
	}
	
	@RequestMapping(value = "/hi", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody	
    public String hi() {
		LOGGER.info("NpvrREST.hi()");
		return "Welcome to RSystems !!!";
	}

	private static HashMap<String,Channel> channels = new HashMap<String,Channel>();	
	static {		
		for(int i=0; i<=10; i++){
			channels.put(""+i, new Channel(""+i, "CHAN"+i));
		}		
	}
	
	private static HashMap<String,Programme> programmes = new HashMap<String,Programme>();	
	
	static {
		int progDuration=0;
		for(int i=0; i<=100; i++){
			int val = i%4;			
			switch(val){
			case 0:
				progDuration = 5;
				break;
			case 1:
				progDuration = 10;
				break;
			case 2:
				progDuration = 20;
				break;
			case 3:
				progDuration = 30;
			}
			programmes.put(""+i, new Programme(""+i, "PROGRAMME"+i, progDuration));
		}
		
	}	
	
	
	public Channel getChannel(String channelId)  throws NpvrException {			
		return channels.get(channelId);
	}
	
	public Programme getProgramme(String programId)  throws NpvrException {			
		return programmes.get(programId);
	}
}
