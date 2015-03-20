namespace java com.milandb.server.thriftjava
#@namespace scala com.milandb.server.thriftscala

exception MilanStoreException {
  1: string description;
}

service MilanStoreService {
   string get(1: string key) throws(1: MilanStoreException ex)
   void put(1: string key, 2: string value)
}
