package com.wetroad.ws.userservice.wsapi;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wetroad.ws.userservice.japi.vo.AppResponse;

import spark.Response;
import spark.ResponseTransformer;

public class AppResponseTransformer implements ResponseTransformer
{

	private static Logger logger = LoggerFactory.getLogger(AppResponseTransformer.class);
	/**
	 * OuterMap: 
	 * 		key - <AppResponse.status>~<AppResponse.apiCallName>~<AppResponse.reasoCode[0]>
	 * 				except for first element, all other elements of the key are optional including separators
	 * 		valueMap: 
	 * 			key - HTTP_STATUS, BODY_TEMPLATE
	 * 			values - valid http status code or body template
	 */
	private Map<String, Map<String, String>> responseMap = null;

	private static final ObjectMapper om = new ObjectMapper();

	public Map<String, Map<String, String>> getResponseMap() {
		return responseMap;
	}

	public void setResponseMap(Map<String, Map<String, String>> responseMap) {
		this.responseMap = responseMap;
	}

	@Override
	public String render(Object arg0) throws Exception {
		Map<String, Object> ret = (Map<String, Object>) arg0;
		AppResponse appResponse = (AppResponse) ret.get("APPRESPONSE");
		if (appResponse == null)
		{
			throw new RuntimeException("AppResponse is null");
		}
		if (appResponse.status == null)
		{
			throw new RuntimeException("AppResponse status is null");
		}
		Response res = (Response) ret.get("RESPONSE");

		// success - convert to JSON body with 200 http code

		// validation failure lookup by service name and set the http status and body
		// with reason codes

		// business failure lookup by service name and reason code and set the http
		// status and body with reason codes

		logger.info("appResponse:" + appResponse);
		String reasonCode = "";
		if (appResponse.reasonCodes != null && appResponse.reasonCodes.get(0) != null)
		{
			reasonCode = "~" + appResponse.reasonCodes.get(0);
		}

		logger.info("reasonCode:" + reasonCode);
		logger.info("responseMap:" + responseMap);
		Map<String, String> responseConf = null;
		if (this.responseMap.containsKey(appResponse.status + "~" + appResponse.apiCallName + reasonCode))
		{
			responseConf = responseMap.get(appResponse.status + "~" + appResponse.apiCallName + reasonCode);
		}
		else if (this.responseMap.containsKey(appResponse.status + "~" + appResponse.apiCallName))
		{
			responseConf = responseMap.get(appResponse.status + "~" + appResponse.apiCallName);
		}
		else if (this.responseMap.containsKey(appResponse.status.toString()))
		{
			responseConf = responseMap.get(appResponse.status.toString());
		}
		else
		{
			throw new RuntimeException("configuration missing");
		}

		res.status(Integer.parseInt(responseConf.get("HTTP_STATUS")));
		res.header("x-request-id", appResponse.requestId);
		res.header("Content-Type", "application/JSON");

		String body = null;
		if (AppResponse.Status.SUCCESS.equals(appResponse.status))
		{
			if (appResponse.object == null || appResponse.object instanceof Void)
				body = "{}";
			else
				body = om.writeValueAsString(appResponse.object);
		}
		else
		{
			body = responseConf.get("BODY_TEMPLATE");
			logger.info("body:" + body);
			body = body.replace("{CALL_NAME}", appResponse.apiCallName);
			logger.info("body:" + body);
			body = body.replace("{REASON_CODES}", "" + appResponse.reasonCodes);
			logger.info("body:" + body);

		}
		return body;
	}

}
