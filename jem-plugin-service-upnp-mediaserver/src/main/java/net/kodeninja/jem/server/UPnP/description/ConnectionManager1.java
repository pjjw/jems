package net.kodeninja.jem.server.UPnP.description;

import java.util.Map;

import net.kodeninja.UPnP.description.Service;
import net.kodeninja.UPnP.description.ServiceAction;
import net.kodeninja.UPnP.description.ServiceActionArgument;
import net.kodeninja.UPnP.description.ServiceStateVariable;
import net.kodeninja.UPnP.description.ServiceStateVariableAllowedValueList;
import net.kodeninja.UPnP.identifiers.SSDPVersion;
import net.kodeninja.UPnP.identifiers.ServiceTypeURN;
import net.kodeninja.UPnP.internal.control.ControlException;

public class ConnectionManager1 extends Service {

	protected static final String Direction_Input = "Input";
	protected static final String Direction_Output = "Output";
	
	protected static final String Status_OK = "OK";
	protected static final String Status_ContentFormatMismatch = "ContentFormatMismatch";
	protected static final String Status_InsufficientBandwidth = "InsufficientBandwidth";
	protected static final String Status_UnreliableChannel = "UnreliableChannel";
	protected static final String Status_Unknown = "Unknown";
	
	ServiceStateVariable SourceProtocolInfo, SinkProtocolInfo, CurrentConnectionIDs,
	A_ARG_TYPE_ConnectionStatus, A_ARG_TYPE_ConnectionManager, A_ARG_TYPE_Direction,
	A_ARG_TYPE_ProtocolInfo, A_ARG_TYPE_ConnectionID, A_ARG_TYPE_AVTransportID,
	A_ARG_TYPE_RcsID;
	
	ServiceAction GetProtocolInfo, GetCurrentConnectionIDs, GetCurrentConnectionInfo;
	
	ServiceActionArgument GetProtocolInfo_Source, GetProtocolInfo_Sink, GetCurrentConnectionIDs_ConnectionIDs,
	GetCurrentConnectionInfo_ConnectionID, GetCurrentConnectionInfo_RcsID, GetCurrentConnectionInfo_AVTransportID,
	GetCurrentConnectionInfo_ProtocolInfo, GetCurrentConnectionInfo_PeerConnectionManager,
	GetCurrentConnectionInfo_PeerConnectionID, GetCurrentConnectionInfo_Direction, GetCurrentConnectionInfo_Status;
	
	public ConnectionManager1(MediaServer1 owner) {
		super(owner, new ServiceTypeURN("ConnectionManager", new SSDPVersion(1)));
		
		//Setup State Variable Table
		ServiceStateVariableAllowedValueList connectionStatusValueList = new ServiceStateVariableAllowedValueList();
		connectionStatusValueList.addValue(Status_OK);
		connectionStatusValueList.addValue(Status_ContentFormatMismatch);
		connectionStatusValueList.addValue(Status_InsufficientBandwidth);
		connectionStatusValueList.addValue(Status_UnreliableChannel);
		connectionStatusValueList.addValue(Status_Unknown);
		
		ServiceStateVariableAllowedValueList direction = new ServiceStateVariableAllowedValueList();
		direction.addValue(Direction_Input);
		direction.addValue(Direction_Output);
		
		setStateVarValue(SourceProtocolInfo = new ServiceStateVariable("SourceProtocolInfo", "string", ""),
				"http-get:*:audio/L16;rate=44100;channels=2:*http-get:*:audio/lpcm:*,http-get:*:image/jpeg:*," +
				"http-get:*:audio/mpeg:*,http-get:*:audio/x-mpeg:*,http-get:*:audio/mpg:*,http-get:*:audio/mp3:*," +
				"http-get:*:video/mpeg:*,http-get:*:video/mpeg2:*,http-get:*:video/x-mpeg:*,http-get:*:video/mpg:*," +
				"http-get:*:video/MP1S:*,http-get:*:audio/mpegurl:*,http-get:*:audio/x-mpegurl:*");
				// "http-get:*:audio/mpeg:*");
		setStateVarValue(SinkProtocolInfo = new ServiceStateVariable("SinkProtocolInfo", "string", ""), "");
		setStateVarValue(CurrentConnectionIDs = new ServiceStateVariable("CurrentConnectionIDs", "string", ""), "0");
		addStateVar(A_ARG_TYPE_ConnectionStatus = new ServiceStateVariable("A_ARG_TYPE_ConnectionStatus", "string", connectionStatusValueList, false));
		addStateVar(A_ARG_TYPE_ConnectionManager = new ServiceStateVariable("A_ARG_TYPE_ConnectionManager", "string", false));
		addStateVar(A_ARG_TYPE_Direction = new ServiceStateVariable("A_ARG_TYPE_Direction", "string", direction, false));
		addStateVar(A_ARG_TYPE_ProtocolInfo = new ServiceStateVariable("A_ARG_TYPE_ProtocolInfo", "string", false));
		addStateVar(A_ARG_TYPE_ConnectionID = new ServiceStateVariable("A_ARG_TYPE_ConnectionID", "i4", false));
		addStateVar(A_ARG_TYPE_AVTransportID = new ServiceStateVariable("A_ARG_TYPE_AVTransportID", "i4", false));
		addStateVar(A_ARG_TYPE_RcsID = new ServiceStateVariable("A_ARG_TYPE_RcsID", "i4", false));
		
		//Setup Action list
		actions.add(GetProtocolInfo = new ServiceAction("GetProtocolInfo"));
		
		GetProtocolInfo.addArg(GetProtocolInfo_Source = new ServiceActionArgument("Source", false, false, SourceProtocolInfo));
		GetProtocolInfo.addArg(GetProtocolInfo_Sink = new ServiceActionArgument("Sink", false, false, SinkProtocolInfo));

		actions.add(GetCurrentConnectionIDs = new ServiceAction("GetCurrentConnectionIDs"));
		GetCurrentConnectionIDs.addArg(GetCurrentConnectionIDs_ConnectionIDs = new ServiceActionArgument("ConnectionIDs", false, false, CurrentConnectionIDs));
		
		actions.add(GetCurrentConnectionInfo = new ServiceAction("GetCurrentConnectionInfo"));
		GetCurrentConnectionInfo.addArg(GetCurrentConnectionInfo_ConnectionID = new ServiceActionArgument("ConnectionID", true, false, A_ARG_TYPE_ConnectionID));
		GetCurrentConnectionInfo.addArg(GetCurrentConnectionInfo_RcsID = new ServiceActionArgument("RcsID", false, false, A_ARG_TYPE_RcsID));
		GetCurrentConnectionInfo.addArg(GetCurrentConnectionInfo_AVTransportID = new ServiceActionArgument("AVTransportID", false, false, A_ARG_TYPE_AVTransportID)); 
		GetCurrentConnectionInfo.addArg(GetCurrentConnectionInfo_ProtocolInfo = new ServiceActionArgument("ProtocolInfo", false, false, A_ARG_TYPE_ProtocolInfo));
		GetCurrentConnectionInfo.addArg(GetCurrentConnectionInfo_PeerConnectionManager = new ServiceActionArgument("PeerConnectionManager", false, false, A_ARG_TYPE_ConnectionManager)); 
		GetCurrentConnectionInfo.addArg(GetCurrentConnectionInfo_PeerConnectionID = new ServiceActionArgument("PeerConnectionID", false, false, A_ARG_TYPE_ConnectionID));
		GetCurrentConnectionInfo.addArg(GetCurrentConnectionInfo_Direction = new ServiceActionArgument("Direction", false, false, A_ARG_TYPE_Direction));
		GetCurrentConnectionInfo.addArg(GetCurrentConnectionInfo_Status = new ServiceActionArgument("Status", false, false, A_ARG_TYPE_ConnectionStatus));
		
	}

	@Override
	public void processAction(ServiceAction action,
			Map<ServiceActionArgument, Object> inArgs,
			Map<ServiceActionArgument, Object> outArgs) throws ControlException {
		
		if (action == GetProtocolInfo)
			getProtocolInfoAction(inArgs, outArgs);
		else if (action == GetCurrentConnectionIDs)
			getCurrentConnectionIDsAction(inArgs, outArgs);
		else if (action == GetCurrentConnectionInfo)
			getCurrentConnectionInfoAction(inArgs, outArgs);
		else {
			System.err.println("ConnectionManager:1 - Unimplemented Action: " + action);
		}
	}

	protected boolean getProtocolInfoAction(Map<ServiceActionArgument, Object> inArgs,
			Map<ServiceActionArgument, Object> outArgs) throws ControlException {
		outArgs.put(GetProtocolInfo_Source, getStateVarValue(SourceProtocolInfo));
		outArgs.put(GetProtocolInfo_Sink, getStateVarValue(SinkProtocolInfo));
		return true;
	}
	
	protected boolean getCurrentConnectionIDsAction(Map<ServiceActionArgument, Object> inArgs,
			Map<ServiceActionArgument, Object> outArgs) throws ControlException {
		outArgs.put(GetCurrentConnectionIDs_ConnectionIDs, getStateVarValue(CurrentConnectionIDs));
		return true;
	}
	
	protected boolean getCurrentConnectionInfoAction(Map<ServiceActionArgument, Object> inArgs,
			Map<ServiceActionArgument, Object> outArgs) throws ControlException {
		outArgs.put(GetCurrentConnectionInfo_RcsID, -1);
		outArgs.put(GetCurrentConnectionInfo_AVTransportID, -1);
		outArgs.put(GetCurrentConnectionInfo_ProtocolInfo, "");
		outArgs.put(GetCurrentConnectionInfo_PeerConnectionManager, "");
		outArgs.put(GetCurrentConnectionInfo_PeerConnectionID, -1);
		outArgs.put(GetCurrentConnectionInfo_Direction, Direction_Output);
		outArgs.put(GetCurrentConnectionInfo_Status, Status_Unknown);
		return true;
	}
	
}
