package com.example;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOServer;
import org.jpos.iso.ISOUtil;
import org.jpos.q2.QBeanSupport;
import org.jpos.q2.iso.QMUX;
import org.jpos.space.Space;
import org.jpos.space.SpaceFactory;
import org.jpos.space.SpaceListener;
import org.jpos.space.SpaceUtil;
import org.jpos.util.NameRegistrar;
import org.jpos.util.NameRegistrar.NotFoundException;

public class LogonManager extends QBeanSupport implements SpaceListener {

    private Space space = SpaceFactory.getSpace();
//	private Space psp = SpaceFactory.getSpace("jdbm:MyTxnSpaceClient");

    private static final String TRACE = "JPTS_TRACE";
    private static final String LOGON = "JPTS_LOGON.";
    private static final String ECHO = "JPTS_ECHO.";
    private long echoInterval = 0;
    private long logonInterval = 0;
    protected QMUX mux = null;
    protected ISOServer isoServer = null;
    protected String readyKey = "";
    private Thread echoThread;
    private Thread logonThread;


    public LogonManager(String muxName, String channelAdaptorName, int intervalEcho) {
        super();
    }

    protected void startService() throws ISOException, NotFoundException {
        echoInterval = Long.parseLong(cfg.get("echo-interval"));
        logonInterval = Long.parseLong(cfg.get("logon-interval"));

        String muxName = cfg.get( "mux" );
        mux = (QMUX) NameRegistrar.getIfExists("mux." + muxName );

        try{
            if( cfg.get( "is-server" ).equals( "true" ) )
                isoServer = ISOServer.getServer( muxName.substring( 0, muxName.length()-4 ) );
        }catch (Exception e) {
            getLog().warn(e);
        }

        readyKey = cfg.get("channel-ready");

        getLog().info(readyKey);

        logonThread = new Thread(new LogonHandler());
        logonThread.start();

        ISOUtil.sleep( 2000 );

        echoThread = new Thread( new EchoHandler() );
        echoThread.start();

        System.out.println( "END" );
    }

    private boolean doEcho() {
        readyKey = cfg.get("channel-ready");

        try{
            if( isoServer != null ) if( isoServer.getActiveConnections() == 0 ) return false;

            System.out.println( "send echo" );
            ISOMsg resp = mux.request( getEchoRequest(), cfg.getLong("timeout"));
            System.out.println( "respon echo" );

            if (resp != null) {
                NameRegistrar.register(ECHO + readyKey, true);
            } else {
                NameRegistrar.register(LOGON + readyKey, false);
                return false;
            }
        } catch (ISOException e) {
            getLog().warn(e);
            return false;
        }

        return true;
    }

    private boolean doLogon() {
        readyKey = cfg.get("channel-ready");
        ISOMsg resp = null;
        try {
            if( isoServer != null ) if( isoServer.getActiveConnections() == 0 ) return false;

            System.out.println( "send logon" );
            resp = mux.request( getSignOnRequest(), cfg.getLong("timeout"));
            System.out.println( "response logon" );

            if (resp != null) {
                NameRegistrar.register(LOGON + readyKey, true);
            } else {
                System.err.println(  "resp == NULL" );
                NameRegistrar.register(LOGON + readyKey, false);
                return false;
            }
        } catch (ISOException e) {
            getLog().warn(e);
            return false;
        }

        return true;
    }


    public class EchoHandler implements Runnable {
        public EchoHandler() {
            super();
        }

        public void run() {


            readyKey = cfg.get("channel-ready");
            System.out.println( "readyKey : "+readyKey );
            while (running()) {
//				trace.print( "timeout "+cfg.getLong("timeout") );
//				Object sessionId = sp.rd(readyKey, cfg.getLong("timeout"));
//				trace.print( "sessionId "+sessionId );
//
//				if (sessionId == null) {
//					getLog().info("Channel " + readyKey + " not ready");
//					NameRegistrar.register(LOGON + readyKey, false);
//					continue;
//				}
                boolean sendSuccess = false;
                Boolean registered = (Boolean) NameRegistrar.getIfExists(LOGON + readyKey);
                try {
                    if (registered == null) {
                        sendSuccess = doLogon();
                    } else if (registered.equals(true)) {
                        sendSuccess = doEcho();
                    } else {
                        sendSuccess = doLogon();
                    }
                } catch (Throwable t) {
                    getLog().warn(t);
                }

                System.out.println( "sendSuccess :: "+sendSuccess );
                if( sendSuccess )
                    ISOUtil.sleep(echoInterval);
                else
                    ISOUtil.sleep( 3000 );
            }
        }

    }

    public class LogonHandler implements Runnable {

        public LogonHandler() {
            super();
        }

        public void run() {


            readyKey = cfg.get("channel-ready");
            System.out.println( "readyKey : "+readyKey );
            while (running()) {
//				trace.print( "ready timeout : "+cfg.getLong("timeout") );
//				getLog().info("Channel " + readyKey + " running ready");
//				Object sessionId = sp.rd(readyKey, cfg.getLong("timeout"));
//
//				if (sessionId == null) {
//					getLog().info("Channel " + readyKey + " not ready");
//					NameRegistrar.register(LOGON + readyKey, false);
//					continue;
//				}

//				if( !mux.isConnected() )
                {
//					ISOUtil.sleep( 1000 );
//					continue;
                }

                boolean sendSuccess = false;
                sendSuccess = doLogon();
                System.out.println( "sendSuccess :: "+sendSuccess );
                if( sendSuccess )
                    ISOUtil.sleep(logonInterval);
                else
                    ISOUtil.sleep( 1000 );
            }
        }
    }

    public void notify(java.lang.Object arg0, java.lang.Object arg1)
    {

    }

    protected ISOMsg getRequestTemplate() throws ISOException {
        ISOMsg isoMsg = new ISOMsg();
        isoMsg.setMTI("0800");
        isoMsg.set(7, UtilityDate.localTransactionTime() );
        isoMsg.set(11, ISOUtil.zeropad (Long.toString (SpaceUtil.nextLong (space, 10000000 ) % 1000000), 6) );
        return isoMsg;
    }

    public ISOMsg getLogOnRequest() throws ISOException {
        ISOMsg isoMsg = getRequestTemplate();
        isoMsg.set(70, "001");
        return isoMsg;
    }

    public ISOMsg getLogOffRequest() throws ISOException {
        ISOMsg isoMsg = getRequestTemplate();
        isoMsg.set(70, "002");
        return isoMsg;
    }

    public ISOMsg getSAFRequest() throws ISOException {
        ISOMsg isoMsg = getRequestTemplate();
        isoMsg.set(70, "060");
        return isoMsg;
    }

    public ISOMsg getEchoRequest() throws ISOException {
        ISOMsg isoMsg = getRequestTemplate();
        isoMsg.set(70, "301");
        return isoMsg;
    }

    public ISOMsg getSignOnRequest() throws ISOException {
        ISOMsg isoMsg = getRequestTemplate();
        isoMsg.set(70, "061");
        return isoMsg;
    }

    public ISOMsg getSignOffRequest() throws ISOException {
        ISOMsg isoMsg = getRequestTemplate();
        isoMsg.set(70, "062");
        return isoMsg;
    }

    public ISOMsg getCutOverRequest() throws ISOException {
        ISOMsg isoMsg = getRequestTemplate();
        isoMsg.set(70, "201");
        return isoMsg;
    }

    public ISOMsg getKeyChangeRequest() throws ISOException {
        ISOMsg isoMsg = getRequestTemplate();
        isoMsg.set(70, "101");
        return isoMsg;
    }

    public ISOMsg getNewKeyRequest() throws ISOException {
        ISOMsg isoMsg = getRequestTemplate();
        isoMsg.set(70, "101");
        return isoMsg;
    }


}

