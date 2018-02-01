package io.pivotal.akitada;

import org.apache.geode.cache.AttributesMutator;
import org.apache.geode.cache.Declarable;
import org.apache.geode.cache.EntryEvent;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.apache.geode.cache.util.CacheListenerAdapter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Properties;

public class GeodeCacheListenerClient <K, V> extends CacheListenerAdapter<K, V> implements Declarable {

  public static void main(String[] argv)  throws Exception {
    System.setProperty("gemfirePropertyFile", "geode-client.properties");
    String regionName = "ExRegion1";
    String locatorHostName = "localhost";
    int locatorListenPort = 55221;

    try {
      regionName = argv[0];
      locatorHostName = argv[1];
      locatorListenPort = Integer.parseInt(argv[2]);
    } catch (Exception ex) {
      System.out.println("Usage: java io.pivotal.akitada.GeodeCacheListenerClient [region name] [locator host name] [locator port]");
      System.exit(1);
    }

    ClientCache ccache = new ClientCacheFactory()
        .set("name","GeodeCacheListenerClient")
        .set("log-level","error")
        .addPoolLocator(locatorHostName, locatorListenPort)
        .setPoolSubscriptionEnabled(true)
        .create();

    ClientRegionFactory rf = ccache.createClientRegionFactory(ClientRegionShortcut.CACHING_PROXY);
    Region<String, String> region = rf.create(regionName);
    AttributesMutator am = region.getAttributesMutator();
    am.addCacheListener(new GeodeCacheListenerClient());
    region.registerInterest("ALL_KEYS");

    System.out.println(region.getFullPath() + " region is created in cache. ");

    System.out.println("--- push any key to stop this client");
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    bufferedReader.readLine();

    ccache.close();
  }

  @Override
  public void afterCreate(EntryEvent<K, V> e) {
    System.out.println("    Received afterCreate event for entry: " + e.getKey() + ", " + e.getNewValue() + ", isOriginRemote=" + e.isOriginRemote());
  }

  @Override
  public void afterUpdate(EntryEvent<K, V> e) {
    System.out.println("    Received afterUpdate event for entry: " + e.getKey() + ", " + e.getNewValue() + ", isOriginRemote=" + e.isOriginRemote());
  }

  @Override
  public void afterDestroy(EntryEvent<K, V> e) {
    System.out.println("    Received afterDestroy event for entry: " + e.getKey() + ", isOriginRemote=" + e.isOriginRemote());
  }

  @Override
  public void afterInvalidate(EntryEvent<K, V> e) {
    System.out.println("    Received afterInvalidate event for entry: " + e.getKey() + ", isOriginRemote=" + e.isOriginRemote());
  }

  @Override
  public void init(Properties props) {
    // do nothing
  }

  @Override
  public void close() {
    System.out.println("    Received close event for cache");
  }
}
