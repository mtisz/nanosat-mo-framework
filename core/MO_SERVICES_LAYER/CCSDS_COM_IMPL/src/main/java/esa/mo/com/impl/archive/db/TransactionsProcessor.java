/* ----------------------------------------------------------------------------
 * Copyright (C) 2015      European Space Agency
 *                         European Space Operations Centre
 *                         Darmstadt
 *                         Germany
 * ----------------------------------------------------------------------------
 * System                : ESA NanoSat MO Framework
 * ----------------------------------------------------------------------------
 * Licensed under the European Space Agency Public License, Version 2.0
 * You may not use this file except in compliance with the License.
 *
 * Except as expressly set forth in this License, the Software is provided to
 * You on an "as is" basis and without warranties of any kind, including without
 * limitation merchantability, fitness for a particular purpose, absence of
 * defects or errors, accuracy or non-infringement of intellectual property rights.
 * 
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 * ----------------------------------------------------------------------------
 */
package esa.mo.com.impl.archive.db;

import esa.mo.com.impl.archive.entities.COMObjectEntity;
import esa.mo.com.impl.provider.ArchiveManager;
import esa.mo.com.impl.util.HelperCOM;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Query;
import org.ccsds.moims.mo.com.archive.structures.ArchiveQuery;
import org.ccsds.moims.mo.com.archive.structures.PaginationFilter;
import org.ccsds.moims.mo.com.archive.structures.QueryFilter;
import org.ccsds.moims.mo.mal.structures.LongList;

/**
 *
 * @author Cesar Coelho
 */
public class TransactionsProcessor {

    private static final String QUERY_SELECT_ALL = "SELECT PU.objId FROM COMObjectEntity PU WHERE PU.objectTypeId=:objectTypeId AND PU.domainId=:domainId";

    private static final Boolean SAFE_MODE = false;
    private static final Class<COMObjectEntity> CLASS_ENTITY = COMObjectEntity.class;
    private final DatabaseBackend dbBackend;

    // This executor is responsible for the interactions with the db
    private final ExecutorService dbInteractionsExecutor = Executors.newSingleThreadExecutor(new DBBackendThreadFactory("COMArchiveBackendProcessor")); // Guarantees sequential order

    // This executor is expecting "short-lived" runnables that generate Events. 2 Threads just for parallelism
    private final ExecutorService publishEventsExecutor = Executors.newFixedThreadPool(2, new DBBackendThreadFactory("PublishEventsProcessor"));
    private final AtomicBoolean sequencialStoring;

    private final LinkedBlockingQueue<StoreCOMObjectsContainer> storeQueue;

    public TransactionsProcessor(DatabaseBackend dbBackend) {
        this.dbBackend = dbBackend;
        this.storeQueue = new LinkedBlockingQueue<StoreCOMObjectsContainer>();
        this.sequencialStoring = new AtomicBoolean(false);
    }
    
    public void submitExternalTask(final Runnable task){
        this.sequencialStoring.set(false); // Sequential stores can no longer happen otherwise we break order
        
        // Abuse the Events executor for this...
        publishEventsExecutor.execute(task);
    }

    public COMObjectEntity getCOMObject(final Integer objTypeId, final Integer domain, final Long objId) {
        this.sequencialStoring.set(false); // Sequential stores can no longer happen otherwise we break order

        Future<COMObjectEntity> future = dbInteractionsExecutor.submit(new Callable() {
            @Override
            public COMObjectEntity call() {
                dbBackend.createEntityManager();
                final COMObjectEntity perObj = dbBackend.getEM().find(CLASS_ENTITY,
                        COMObjectEntity.generatePK(objTypeId, domain, objId));
                dbBackend.closeEntityManager();
                return perObj;
            }
        });

        try {
            return future.get();
        } catch (InterruptedException ex) {
            Logger.getLogger(TransactionsProcessor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(TransactionsProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public LongList getAllCOMObjects(final Integer objTypeId, final Integer domainId) {
        this.sequencialStoring.set(false); // Sequential stores can no longer happen otherwise we break order

        Future<LongList> future = dbInteractionsExecutor.submit(new Callable() {
            @Override
            public LongList call() {
                dbBackend.createEntityManager();
                Query query = dbBackend.getEM().createQuery(QUERY_SELECT_ALL);
                query.setParameter("objectTypeId", objTypeId);
                query.setParameter("domainId", domainId);
                LongList objIds = new LongList();
                objIds.addAll(query.getResultList());
                dbBackend.closeEntityManager();

                return objIds;
            }
        });

        try {
            return future.get();
        } catch (InterruptedException ex) {
            Logger.getLogger(TransactionsProcessor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(TransactionsProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    private void persistObjects(final ArrayList<COMObjectEntity> perObjs) {
        for (int i = 0; i < perObjs.size(); i++) { // 6.510 ms per cycle
            if (SAFE_MODE) {
                final COMObjectEntity objCOM = dbBackend.getEM().find(CLASS_ENTITY, perObjs.get(i).getPrimaryKey()); // 0.830 ms

                if (objCOM == null) { // Last minute safety, the db crashes if one tries to store an object with a used pk
                    final COMObjectEntity perObj = perObjs.get(i); // The object to be stored  // 0.255 ms
                    dbBackend.getEM().persist(perObj);  // object    // 0.240 ms
                } else {
                    Logger.getLogger(TransactionsProcessor.class.getName()).log(Level.SEVERE,
                            "The Archive could not store the object: " + perObjs.get(i).toString(), new Throwable());
                }
            } else {
                dbBackend.getEM().persist(perObjs.get(i));  // object
            }

            // Flush every 1k objects...
            if (i != 0) {
                if ((i % 1000) == 0) {
                    Logger.getLogger(TransactionsProcessor.class.getName()).log(Level.FINE,
                            "Flushing the data after 1000 serial stores...");
                    dbBackend.getEM().flush();
                    dbBackend.getEM().clear();
                }
            }
        }
    }

    public void insert(final ArrayList<COMObjectEntity> perObjs, final Runnable publishEvents) {
        final boolean isSequential = this.sequencialStoring.get();
        final StoreCOMObjectsContainer container = new StoreCOMObjectsContainer(perObjs, isSequential);

        this.sequencialStoring.set(true);

        try { // Insert into queue
            storeQueue.put(container);
        } catch (InterruptedException ex) {
            Logger.getLogger(ArchiveManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        dbInteractionsExecutor.execute(new Runnable() {
            @Override
            public void run() {
                StoreCOMObjectsContainer container = storeQueue.poll();

                if (container != null) {
                    dbBackend.createEntityManager();  // 0.166 ms
                    dbBackend.getEM().getTransaction().begin(); // 0.480 ms
                    persistObjects(container.getPerObjs()); // store

                    while (true) {
                        container = storeQueue.peek(); // get next if there is one available
                        if (container != null && container.isContinuous()) {
                            container = storeQueue.poll();
                            persistObjects(container.getPerObjs()); // store
                        } else {
                            break;
                        }
                    }

                    dbBackend.safeCommit();
                    dbBackend.closeEntityManager(); // 0.410 ms
                }

                publishEventsExecutor.submit(publishEvents);
            }
        });
    }

    public void remove(final Integer objTypeId, final Integer domainId,
            final LongList objIds, final Runnable publishEvents) {
        this.sequencialStoring.set(false); // Sequential stores can no longer happen otherwise we break order

        dbInteractionsExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dbBackend.createEntityManager();  // 0.166 ms

                // Generate the object Ids if needed and the persistence objects to be removed
                for (int i = 0; i < objIds.size(); i++) {
                    final COMObjectEntityPK id = COMObjectEntity.generatePK(objTypeId, domainId, objIds.get(i));
                    COMObjectEntity perObj = dbBackend.getEM().find(CLASS_ENTITY, id);
                    dbBackend.getEM().getTransaction().begin();
                    dbBackend.getEM().remove(perObj);
                    dbBackend.getEM().getTransaction().commit();
                }

                dbBackend.closeEntityManager(); // 0.410 ms

                publishEventsExecutor.submit(publishEvents);
            }
        });
    }

    public void update(final ArrayList<COMObjectEntity> newObjs, final Runnable publishEvents) {
        this.sequencialStoring.set(false); // Sequential stores can no longer happen otherwise we break order

        dbInteractionsExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dbBackend.createEntityManager();  // 0.166 ms

                for (int i = 0; i < newObjs.size(); i++) {
                    final COMObjectEntityPK id = newObjs.get(i).getPrimaryKey();
                    COMObjectEntity previousObj = dbBackend.getEM().find(CLASS_ENTITY, id);

                    dbBackend.getEM().getTransaction().begin();
                    dbBackend.getEM().remove(previousObj);
                    dbBackend.getEM().getTransaction().commit();

                    // Maybe we can replace the 3 lines below with a persistObjects(newObjs) after the for loop
                    dbBackend.getEM().getTransaction().begin();
                    dbBackend.getEM().persist(newObjs.get(i));
                    dbBackend.getEM().getTransaction().commit();
                }

                dbBackend.closeEntityManager(); // 0.410 ms

                publishEventsExecutor.submit(publishEvents);
            }
        });
    }

    private class QueryCallable implements Callable {

        private final Integer objTypeId;
        private final ArchiveQuery archiveQuery;
        private final Integer domainId;
        private final Integer providerURIId;
        private final Integer networkId;
        private final SourceLinkContainer sourceLink;
        private final QueryFilter filter;
        
        public QueryCallable(final Integer objTypeId,
                final ArchiveQuery archiveQuery, final Integer domainId,
                final Integer providerURIId, final Integer networkId,
                final SourceLinkContainer sourceLink, final QueryFilter filter) {
            this.objTypeId = objTypeId;
            this.archiveQuery = archiveQuery;
            this.domainId = domainId;
            this.providerURIId = providerURIId;
            this.networkId = networkId;
            this.sourceLink = sourceLink;
            this.filter = filter;
        }

        @Override
        public ArrayList<COMObjectEntity> call() {
            final boolean domainContainsWildcard = HelperCOM.domainContainsWildcard(archiveQuery.getDomain());
            final boolean objectTypeContainsWildcard = (objTypeId == 0);
            final boolean relatedContainsWildcard = (archiveQuery.getRelated().equals((long) 0));
            final boolean startTimeContainsWildcard = (archiveQuery.getStartTime() == null);
            final boolean endTimeContainsWildcard = (archiveQuery.getEndTime() == null);
            final boolean providerURIContainsWildcard = (archiveQuery.getProvider() == null);
            final boolean networkContainsWildcard = (archiveQuery.getNetwork() == null);

            final boolean sourceContainsWildcard = (archiveQuery.getSource() == null);
            boolean sourceTypeContainsWildcard = true;
            boolean sourceDomainContainsWildcard = true;
            boolean sourceObjIdContainsWildcard = true;

            if (!sourceContainsWildcard) {
                sourceTypeContainsWildcard = ArchiveManager.objectTypeContainsWildcard(archiveQuery.getSource().getType());
                sourceDomainContainsWildcard = HelperCOM.domainContainsWildcard(archiveQuery.getSource().getKey().getDomain());
                sourceObjIdContainsWildcard = (archiveQuery.getSource().getKey().getInstId() == null || archiveQuery.getSource().getKey().getInstId() == 0);
            }

            final boolean hasSomeDefinedFields
                    = (!domainContainsWildcard
                    || !objectTypeContainsWildcard
                    || !relatedContainsWildcard
                    || !startTimeContainsWildcard
                    || !endTimeContainsWildcard
                    || !providerURIContainsWildcard
                    || !networkContainsWildcard
                    || !sourceContainsWildcard);

            // Generate the query string
            String strPU = "objectTypeId, objId, domainId, network, OBJ, providerURI, "
                    + "relatedLink, sourceLinkDomainId, sourceLinkObjId, sourceLinkObjectTypeId, timestampArchiveDetails";
            String queryString = "SELECT " + strPU + " FROM COMObjectEntity PU ";
            
            queryString += (hasSomeDefinedFields) ? "WHERE " : "";

            queryString += (domainContainsWildcard) ? "" : "PU.domainId=" + domainId + " AND ";
            queryString += (objectTypeContainsWildcard) ? "" : "PU.objectTypeId=" + objTypeId + " AND ";
            queryString += (relatedContainsWildcard) ? "" : "PU.relatedLink=" + archiveQuery.getRelated() + " AND ";
            queryString += (startTimeContainsWildcard) ? "" : "PU.timestampArchiveDetails>=" + archiveQuery.getStartTime().getValue() + " AND ";
            queryString += (endTimeContainsWildcard) ? "" : "PU.timestampArchiveDetails<=" + archiveQuery.getEndTime().getValue() + " AND ";
            queryString += (providerURIContainsWildcard) ? "" : "PU.providerURI=" + providerURIId + " AND ";
            queryString += (networkContainsWildcard) ? "" : "PU.network=" + networkId + " AND ";

            if (!sourceContainsWildcard) {
                queryString += (sourceTypeContainsWildcard) ? "" : "PU.sourceLinkObjectTypeId=" + sourceLink.getObjectTypeId() + " AND ";
                queryString += (sourceDomainContainsWildcard) ? "" : "PU.sourceLinkDomainId=" + sourceLink.getDomainId() + " AND ";
                queryString += (sourceObjIdContainsWildcard) ? "" : "PU.sourceLinkObjId=" + sourceLink.getObjId() + " AND ";
            }

            if (hasSomeDefinedFields) { // 4 because we are removing the "AND " part
                queryString = queryString.substring(0, queryString.length() - 4);
            }
            
            if (filter != null) {
                if(filter instanceof PaginationFilter){
                    PaginationFilter pfilter = (PaginationFilter) filter;

                    // Double check if the filter fields are really not null
                    if(pfilter.getLimit() != null && pfilter.getOffset() != null){
                        queryString += " LIMIT " + pfilter.getLimit().getValue() + " OFFSET " + pfilter.getOffset().getValue();
                    }
                }
            }
            
            
            /*
            String queryString = "SELECT PU FROM COMObjectEntity PU ";
            queryString += (hasSomeDefinedFields) ? "WHERE " : "";

            queryString += (domainContainsWildcard) ? "" : "PU.domainId=:domainId AND ";
            queryString += (objectTypeContainsWildcard) ? "" : "PU.objectTypeId=:objectTypeId AND ";
            queryString += (relatedContainsWildcard) ? "" : "PU.relatedLink=:relatedLink AND ";
            queryString += (startTimeContainsWildcard) ? "" : "PU.timestampArchiveDetails>=:startTime AND ";
            queryString += (endTimeContainsWildcard) ? "" : "PU.timestampArchiveDetails<=:endTime AND ";
            queryString += (providerURIContainsWildcard) ? "" : "PU.providerURI=:providerURI AND ";
            queryString += (networkContainsWildcard) ? "" : "PU.network=:network AND ";

            if (!sourceContainsWildcard) {
                queryString += (sourceTypeContainsWildcard) ? "" : "PU.sourceLinkObjectTypeId=:sourceLinkObjectTypeId AND ";
                queryString += (sourceDomainContainsWildcard) ? "" : "PU.sourceLinkDomainId=:sourceLinkDomainId AND ";
                queryString += (sourceObjIdContainsWildcard) ? "" : "PU.sourceLinkObjId=:sourceLinkObjId AND ";
            }

            if (hasSomeDefinedFields) { // 4 because we are removing the "AND " part
                queryString = queryString.substring(0, queryString.length() - 4);
            }
            */
            

            dbBackend.createEntityManager();
/*            
            Query query = dbBackend.getEM().createQuery(queryString); // Make the query
//            String PU = "objectTypeId, objId, domainId, network, OBJ, providerURI, relatedLink, sourceLinkDomainId, sourceLinkObjId, sourceLinkObjectTypeId, timestampArchiveDetails";
//            Query query = dbBackend.getEM().createNativeQuery("SELECT " + PU + " PU FROM COMObjectEntity PU LIMIT 5 OFFSET 1"); // Make the query
//            Query query = dbBackend.getEM().createNativeQuery("SELECT PU FROM COMObjectEntity PU LIMIT 5 OFFSET 1"); // Make the query

            if (!objectTypeContainsWildcard) {
                query.setParameter("objectTypeId", objTypeId);
            }

            if (!domainContainsWildcard) {
                query.setParameter("domainId", domainId);
            }

            if (!relatedContainsWildcard) {
                query.setParameter("relatedLink", archiveQuery.getRelated());
            }

            if (!startTimeContainsWildcard) {
                query.setParameter("startTime", archiveQuery.getStartTime().getValue());
            }

            if (!endTimeContainsWildcard) {
                query.setParameter("endTime", archiveQuery.getEndTime().getValue());
            }

            if (!providerURIContainsWildcard) {
                query.setParameter("providerURI", providerURIId);
            }

            if (!networkContainsWildcard) {
                query.setParameter("network", networkId);
            }

            if (!sourceTypeContainsWildcard) {
                query.setParameter("sourceLinkObjectTypeId", sourceLink.getObjectTypeId());
            }

            if (!sourceDomainContainsWildcard) {
                query.setParameter("sourceLinkDomainId", sourceLink.getDomainId());
            }

            if (!sourceObjIdContainsWildcard) {
                query.setParameter("sourceLinkObjId", sourceLink.getObjId());
            }
*/
            /*
            if(paginationFilterEnabled){
                query.setParameter("limit", pfilter.getLimit().getValue());
                query.setParameter("offset", pfilter.getOffset().getValue());
            }
            */

            Thread tThread = new Thread(new Runnable() { // Display a message in case the query gets stuck...
                @Override
                public void run() {
                    try {
                        this.wait(10 * 1000); // 10 seconds
                        Logger.getLogger(ArchiveManager.class.getName()).log(Level.INFO,
                                "The query is taking longer than 10 seconds. "
                                + "The query might be too broad to be handled by the database.");
                    } catch (InterruptedException ex) {
                    } catch (IllegalMonitorStateException ex) {
                    }
                }
            });

            tThread.start();

            final Query query = dbBackend.getEM().createNativeQuery(queryString);
           
            // BUG: The code will get stuck on the line below if the database is too big (tested with Derby)
            final List resultList = query.getResultList();

            dbBackend.closeEntityManager();
            
            // FYI: SELECT objectTypeId, objId, domainId, network, OBJ, providerURI, relatedLink, 
            // sourceLinkDomainId, sourceLinkObjId, sourceLinkObjectTypeId, timestampArchiveDetails FROM COMObjectEntity
            ArrayList<COMObjectEntity> perObjs = new ArrayList<COMObjectEntity>(resultList.size());
            
            for(Object obj: resultList) {
                // (final Integer objectTypeId, final Integer domainId, final Long objId)
                final SourceLinkContainer source = new SourceLinkContainer(
                        (Integer) ((Object[]) obj)[9],
                        (Integer) ((Object[]) obj)[7],
                        convert2Long(((Object[]) obj)[8]));
                
                COMObjectEntity entity = new COMObjectEntity(
                    (Integer) ((Object[]) obj)[0],
                    (Integer) ((Object[]) obj)[2],
                    convert2Long(((Object[]) obj)[1]),
                    convert2Long(((Object[]) obj)[10]),
                    (Integer) ((Object[]) obj)[5],
                    (Integer) ((Object[]) obj)[3],
                    source,
                    convert2Long(((Object[]) obj)[6]),
                    (byte[]) ((Object[]) obj)[4]
                );
                
                perObjs.add(entity);
            }
                        
            tThread.interrupt();

            return perObjs;
        }
    }

    public ArrayList<COMObjectEntity> query(final Integer objTypeId,
            final ArchiveQuery archiveQuery, final Integer domainId,
            final Integer providerURIId, final Integer networkId,
            final SourceLinkContainer sourceLink, final QueryFilter filter) {
        this.sequencialStoring.set(false); // Sequential stores can no longer happen otherwise we break order
        final QueryCallable task = new QueryCallable(objTypeId, archiveQuery,
                domainId, providerURIId, networkId, sourceLink, filter);

        Future<ArrayList<COMObjectEntity>> future = dbInteractionsExecutor.submit(task);

        try {
            return future.get();
        } catch (InterruptedException ex) {
            Logger.getLogger(TransactionsProcessor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(TransactionsProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public void resetMainTable(final Callable task) {
        this.sequencialStoring.set(false); // Sequential stores can no longer happen otherwise we break order
        Future<Integer> nullValue = dbInteractionsExecutor.submit(task);
        Logger.getLogger(TransactionsProcessor.class.getName()).info("Reset table submitted!");

        try {
            Integer dummyInt = nullValue.get(); // Dummy code to Force a wait until the actual restart is done!
            Logger.getLogger(TransactionsProcessor.class.getName()).info("Reset table callback!");
        } catch (InterruptedException ex) {
            Logger.getLogger(TransactionsProcessor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(TransactionsProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void stopInteractions(final Callable task) {
        this.sequencialStoring.set(false); // Sequential stores can no longer happen otherwise we break order
        Future<Integer> nullValue = dbInteractionsExecutor.submit(task);

        try {
            nullValue.get(); // Dummy code to Force a wait until the actual restart is done!
        } catch (InterruptedException ex) {
            Logger.getLogger(TransactionsProcessor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(TransactionsProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * The database backend thread factory
     */
    static class DBBackendThreadFactory implements ThreadFactory {

        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        DBBackendThreadFactory(String prefix) {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup()
                    : Thread.currentThread().getThreadGroup();
            namePrefix = prefix + "-thread-";
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r,
                    namePrefix + threadNumber.getAndIncrement(),
                    0);
            if (t.isDaemon()) {
                t.setDaemon(false);
            }
            if (t.getPriority() != Thread.NORM_PRIORITY) {
                t.setPriority(Thread.NORM_PRIORITY);
            }
            return t;
        }
    }
    
    private static Long convert2Long(final Object obj){
        if(obj == null){
            return null;
        }
        
        if (obj instanceof Long){
            return (Long) obj;
        }

        if (obj instanceof Integer){
            return ((Integer) obj).longValue();
        }
        
        return null;
    }

}