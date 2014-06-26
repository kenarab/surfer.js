import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.webapp.WebAppContext;
import org.mortbay.thread.BoundedThreadPool;

public class JettyServer
{
    protected static final int MAX_TIME= 7000000;

    public static void main(String[] args) throws Exception
    {
	Server server= new Server();
	BoundedThreadPool threadPool= new BoundedThreadPool();
	threadPool.setMaxThreads(100);
	server.setThreadPool(threadPool);

	Connector connector= new SelectChannelConnector();
	connector.setPort(8081);
	connector.setMaxIdleTime(MAX_TIME);
	server.setConnectors(new Connector[] { connector });

	
	server.addHandler(new WebAppContext("war", "/surf.js"));

	server.setStopAtShutdown(true);
	server.setSendServerVersion(true);
	server.start();
	server.join();
    }
}