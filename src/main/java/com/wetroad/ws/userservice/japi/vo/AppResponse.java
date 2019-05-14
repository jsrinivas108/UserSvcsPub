package com.wetroad.ws.userservice.japi.vo;

import java.util.List;

public class AppResponse<T> {

	public enum Status
	{
		SUCCESS,
		VALIDATION_FAILURE,
		BUSINESS_FAILURE
	}
	
	public Status status = null;
	public List<String> reasonCodes = null;
	public T object = null;
	public String apiCallName = null;
	public String requestId = null;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((apiCallName == null) ? 0 : apiCallName.hashCode());
		result = prime * result + ((object == null) ? 0 : object.hashCode());
		result = prime * result + ((reasonCodes == null) ? 0 : reasonCodes.hashCode());
		result = prime * result + ((requestId == null) ? 0 : requestId.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AppResponse other = (AppResponse) obj;
		if (apiCallName == null)
		{
			if (other.apiCallName != null)
				return false;
		} else if (!apiCallName.equals(other.apiCallName))
			return false;
		if (object == null)
		{
			if (other.object != null)
				return false;
		} else if (!object.equals(other.object))
			return false;
		if (reasonCodes == null)
		{
			if (other.reasonCodes != null)
				return false;
		} else if (!reasonCodes.equals(other.reasonCodes))
			return false;
		if (requestId == null)
		{
			if (other.requestId != null)
				return false;
		} else if (!requestId.equals(other.requestId))
			return false;
		if (status != other.status)
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(super.toString()).append(" [status=").append(status).append(", reasonCodes=").append(reasonCodes)
				.append(", object=").append(object).append(", apiCallName=").append(apiCallName).append(", requestId=")
				.append(requestId).append("]");
		return builder.toString();
	}
	
}
