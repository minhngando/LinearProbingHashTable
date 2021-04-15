/*
   Project 3
   Name: Minhngan Do netid: mvd170130
   Class: CS3345.007
   Professor: Greg Ozbirn
*/

class LinearProbingHashTable<K,V> {
   
   private static class Entry<K,V> {
      
      //every entry is a value v, with a key k that'll be inserted in a hash function for sorting
      public K key;
      public V value;
      public boolean isActive = true; 
      
      public Entry(K key, V value) {
         this.key = key;
         this.value = value;
      }
   }
   
   private Entry<K,V> table[];
   private int size = 10;
   private int numEntries = 0;

   
   public LinearProbingHashTable() {
      table = new Entry[size];
   }
   
   public LinearProbingHashTable(int theSize) {
      size = theSize;
      table = new Entry[theSize];
   }
   
   /*
    * returns the hash value for the given key
    * (this is the value before probing occurs)
    * aka, first hash, if first hash spot isn't empty, then probe for next spot
    */
   public int getHashValue(K key) {
      return key.hashCode() % size;
   }
   
   /*
    * returns the location for the given key,
    * or -1 if not found 
    * (this is the value after probing occurs)
    */
   public int getLocation(K key) {
      
      //go through entire table, increment normally
      for(int i = 0; i < size; i++) {
         
         //if spot isn't empty
         if(table[i] != null) {
            //if spot isn't empty && the entry's key matches passed key, then found
            if (table[i].key == key) {
               return i;
            }
         }
      }
      
      return -1;
   }
   /*
    * doubles the table size, hashes everything to
    * the new table, omitting items that are marked deleted
    */
   private void rehash() {
      
      //make a new table twice the size of the original
      int newSize = size * 2;
      LinearProbingHashTable<K,V> table2;
      table2 = new LinearProbingHashTable<K,V>(newSize);
      
      //go through the items of the original table
      for (int i = 0; i < size; i++) {
         //if the item in the original table exists (skip over null and items marked deleted)
         if (table[i] != null && table[i].isActive == true) {
            //insert the item from table1 to table2
            table2.insert(table[i].key, table[i].value);
         }
      }
      
      //copy
      size = table2.size;
      table = table2.table;
      
      
   }
   
   /*
    * returns value for key, or null if not found
    */
   public V find(K key) {
      
      //traverse through all of the item's would be hashes and compares the item's key to all of those hash spots to see if it fits
      for (int i = getHashValue(key); table[i] != null; i = (i+1) % size) {
         if (table[i].key == key) {
            return table[i].value;
         }   
      }
      
      //return null if not found
      return null;
   }
   /*
    * inserts entry, rehashes if half full,
    * can re-use deleted entries, throws
    * exception if key is null, returns true
    * if inserted, false if duplicate
    */
   public boolean insert(K key, V value){
      
      //make a new entry to be inserted with the passed key and value
      Entry<K,V> newItem = new Entry<K,V>(key,value);
      
      if(key == null) {
         throw new NullPointerException("Key is null");
      }
      
      if (numEntries >= size/2) {
         rehash();
      }
      
      //find right spot, i starts at the spot the item first hashes to
      //i = (i+1) % size linearly probes the table for the next spot to be inserted
      
      for (int i = getHashValue(key); i < size; i = (i+1) % size) {
         
         //if the spot is empty, insert here
         if(table[i] == null) {
            table[i] = newItem;
            numEntries++;
            return true;
         }
         
         //if the spot isn't empty
         else {
            //check if duplicate
            if(table[i].value == value) {
               return false;
            }

         } 
          
      }
      
      
      return false;
   }
   
   /*
    * marks the entry deleted but leaves it there,
    * returns true if deleted, false if not found
    */ 
   public boolean delete(K key) {
      
      //key of item hashed and probed, if not found, return false bc can't delete  
      if(find(key) == null) {
         return false;
      }
      
      //start at first hash 
      int i = getHashValue(key); 
      
      //keep probing until the passed key that you want deleted matches one in the table
      while(!key.equals(table[i].key)) {
         i = (i+1) % size;
      }
      
      //mark deleted, don't actually make it null
      table[i].isActive = false;
      
      numEntries--;
      
      return true;
   }
   
   public String toString() {
      
      String output = "";
      
      
      for (int i = 0; i < size; i++) {
         
         //if spot isn't empty and item isn't deleted
         if(table[i] != null && table[i].isActive == true) {
            output+= i + ":\t" + table[i].key + "\t" + table[i].value + "\n";
         }
         //if spot isn't empty and item has been deleted
         else if (table[i] != null && table[i].isActive == false) {
            output += i + ":\t" + table[i].key + "\t" + table[i].value + "\t" + "deleted\n";
         }
         //if spot is empty
         else {
            output += i +": \n";
         }
      }
      
      return output;
   }
   
   public static void main(String[] args) {
      
      LinearProbingHashTable<Integer,String> myTable = new LinearProbingHashTable<Integer,String>();
      
      System.out.println("TESTING INSERT");
      
      myTable.insert(6, "Esme");
      myTable.insert(28, "Nina");
      myTable.insert(99, "Romana");
      myTable.insert(11, "Jean Pierre");
      myTable.insert(12, "Pablo");
      System.out.println(myTable.toString());
      
      System.out.println("TESTING REHASH, GETHASHVALUE, GETLOCATION");
      System.out.println("Inserting K=19 V=Marta...");
      myTable.insert(19, "Marta");
      System.out.println("Original hash value: " + myTable.getHashValue(19));
      System.out.println("Location after probe: " + myTable.getLocation(19));
      System.out.println(myTable.toString());
      
      System.out.println("TESTING REHASH, GETHASHVALUE, GETLOCATION");
      System.out.println("Inserting K=26 V=Torres");
      myTable.insert(26, "Torres");
      System.out.println("Original hash value: " + myTable.getHashValue(26));
      System.out.println("Location after probe: " + myTable.getLocation(26));
      System.out.println(myTable.toString());
      
      System.out.println("TESTING FIND, DELETE");
      System.out.println("Finding K=99...");
      System.out.println("The value found at K=99 is: " + myTable.find(99));
      
      System.out.println("Finding K=42...");
      System.out.println("The value found at K=42 is: " + myTable.find(42));
      
      System.out.println("Deleting K=99 V=Romana");
      myTable.delete(99);
      System.out.println(myTable.toString());
   }

}