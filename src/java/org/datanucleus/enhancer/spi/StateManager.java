/**********************************************************************
Copyright (c) 2013 Andy Jefferson and others. All rights reserved.
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

Contributors:
    ...
**********************************************************************/
package org.datanucleus.enhancer.spi;

import javax.jdo.PersistenceManager;

import org.datanucleus.ExecutionContext;

/** 
 * Manager of state of a Persistable object.
 */
public interface StateManager 
{
    /** 
     * The owning <code>StateManager</code> uses this method to supply the 
     * value of the flags to the <code>Persistable</code> instance.
     * @param pc the calling <code>Persistable</code> instance
     * @return the value of <code>jdoFlags</code> to be stored in the 
     * <code>Persistable</code> instance
     */
    byte replacingFlags(Persistable pc);

    /** 
     * Replace the current value of <code>jdoStateManager</code>.
     * <P>
     * This method is called by the <code>Persistable</code> whenever
     * <code>jdoReplaceStateManager</code> is called and there is already
     * an owning <code>StateManager</code>.  This is a security precaution
     * to ensure that the owning <code>StateManager</code> is the only
     * source of any change to its reference in the 
     * <code>Persistable</code>.
     * @return the new value for the <code>jdoStateManager</code>
     * @param pc the calling <code>Persistable</code> instance
     * @param sm the proposed new value for the <code>jdoStateManager</code>
     */ 
    StateManager replacingStateManager (Persistable pc, StateManager sm);
    
    /** 
     * Returns whether the object is dirty.
     * Instances that have been modified, deleted, or newly 
     * made persistent in the current transaction return <code>true</code>.
     *
     *<P>Transient nontransactional instances return <code>false</code>.
     *<P>
     * @see Persistable#dnMakeDirty(String fieldName)
     * @param pc the calling <code>Persistable</code> instance
     * @return <code>true</code> if this instance has been modified in the 
     * current transaction.
     */
    boolean isDirty(Persistable pc);

    /**
     * Returns whether the object is transactional.
     * Instances that respect transaction boundaries return <code>true</code>.  
     * These instances include transient instances made transactional as a 
     * result of being the target of a <code>makeTransactional</code> method 
     * call; newly made persistent or deleted persistent instances; persistent 
     * instances read in data store transactions; and persistent instances 
     * modified in optimistic transactions.
     *
     *<P>Transient nontransactional instances return <code>false</code>.
     *<P>
     * @param pc the calling <code>Persistable</code> instance
     * @return <code>true</code> if this instance is transactional.
     */
    boolean isTransactional(Persistable pc);

    /** Tests whether this object is persistent.
     *
     * Instances whose state is stored in the data store return 
     * <code>true</code>.
     *
     *<P>Transient instances return <code>false</code>.
     *<P>
     * @see PersistenceManager#makePersistent(Object pc)
     * @param pc the calling <code>Persistable</code> instance
     * @return <code>true</code> if this instance is persistent.
     */
    boolean isPersistent(Persistable pc);

    /** Tests whether this object has been newly made persistent.
     *
     * Instances that have been made persistent in the current transaction 
     * return <code>true</code>.
     *
     * <P>Transient instances return <code>false</code>.
     * <P>
     * @see PersistenceManager#makePersistent(Object pc)
     * @param pc the calling <code>Persistable</code> instance
     * @return <code>true</code> if this instance was made persistent
     * in the current transaction.
     */
    boolean isNew(Persistable pc);

    /** Tests whether this object has been deleted.
     *
     * Instances that have been deleted in the current transaction return 
     * <code>true</code>.
     *
     *<P>Transient instances return <code>false</code>.
     *<P>
     * @see PersistenceManager#deletePersistent(Object pc)
     * @param pc the calling <code>Persistable</code> instance
     * @return <code>true</code> if this instance was deleted
     * in the current transaction.
     */
    boolean isDeleted(Persistable pc);

    /** 
     * Return the <code>ExecutionContext</code> that owns this instance.
     * TODO Maybe this should return the EntityManager?
     * @param pc the calling <code>Persistable</code> instance
     * @return the <code>ExecutionContext</code> that owns this instance
     */
    ExecutionContext getExecutionContext(Persistable pc);

    /** 
     * Mark the associated <code>Persistable</code> field dirty.
     * <P>The <code>StateManager</code> will make a copy of the field
     * so it can be restored if needed later, and then mark
     * the field as modified in the current transaction.
     * @param pc the calling <code>Persistable</code> instance
     * @param fieldName the name of the field
     */    
    void makeDirty (Persistable pc, String fieldName);

    /** 
     * Return the object representing the JDO identity 
     * of the calling instance.  If the JDO identity is being changed in
     * the current transaction, this method returns the identity as of
     * the beginning of the transaction.
     * @param pc the calling <code>Persistable</code> instance
     * @return the object representing the JDO identity of the calling instance
     */    
    Object getObjectId (Persistable pc);

    /** 
     * Return the object representing the JDO identity  of the calling instance. 
     * If the JDO identity is being changed in the current transaction, this method 
     * returns the current identity as changed in the transaction.
     * @param pc the calling <code>Persistable</code> instance
     * @return the object representing the JDO identity of the calling instance
     */    
    Object getTransactionalObjectId (Persistable pc);

    /** 
     * Return the object representing the version of the calling instance.
     * @param pc the calling <code>Persistable</code> instance
     * @return the object representing the version of the calling instance
     * @since 2.0
     */    
    Object getVersion (Persistable pc);

    /** 
     * Return <code>true</code> if the field is cached in the calling instance.
     * @param pc the calling <code>Persistable</code> instance
     * @param field the field number
     * @return whether the field is cached in the calling instance
     */    
    boolean isLoaded (Persistable pc, int field);
    
    /** 
     * Guarantee that the serializable transactional and persistent fields
     * are loaded into the instance.  This method is called by the generated
     * <code>jdoPreSerialize</code> method prior to serialization of the instance.
     * @param pc the calling <code>Persistable</code> instance
     */    
    void preSerialize (Persistable pc);
    
    /** 
     * Return the value for the field.
     * @param pc the calling <code>Persistable</code> instance
     * @param field the field number 
     * @param currentValue the current value of the field
     * @return the new value for the field
     */    
    boolean getBooleanField (Persistable pc, int field, boolean currentValue);
    
    /** 
     * Return the value for the field.
     * @param pc the calling <code>Persistable</code> instance
     * @param field the field number 
     * @param currentValue the current value of the field
     * @return the new value for the field
     */    
    char getCharField (Persistable pc, int field, char currentValue);
    
    /** 
     * Return the value for the field.
     * @param pc the calling <code>Persistable</code> instance
     * @param field the field number 
     * @param currentValue the current value of the field
     * @return the new value for the field
     */    
    byte getByteField (Persistable pc, int field, byte currentValue);
    
    /** 
     * Return the value for the field.
     * @param pc the calling <code>Persistable</code> instance
     * @param field the field number 
     * @param currentValue the current value of the field
     * @return the new value for the field
     */    
    short getShortField (Persistable pc, int field, short currentValue);
    
    /** 
     * Return the value for the field.
     * @param pc the calling <code>Persistable</code> instance
     * @param field the field number 
     * @param currentValue the current value of the field
     * @return the new value for the field
     */    
    int getIntField (Persistable pc, int field, int currentValue);
    
    /** 
     * Return the value for the field.
     * @param pc the calling <code>Persistable</code> instance
     * @param field the field number 
     * @param currentValue the current value of the field
     * @return the new value for the field
     */    
    long getLongField (Persistable pc, int field, long currentValue);
    
    /** 
     * Return the value for the field.
     * @param pc the calling <code>Persistable</code> instance
     * @param field the field number 
     * @param currentValue the current value of the field
     * @return the new value for the field
     */    
    float getFloatField (Persistable pc, int field, float currentValue);
    
    /** 
     * Return the value for the field.
     * @param pc the calling <code>Persistable</code> instance
     * @param field the field number 
     * @param currentValue the current value of the field
     * @return the new value for the field
     */    
    double getDoubleField (Persistable pc, int field, double currentValue);
    
    /** 
     * Return the value for the field.
     * @param pc the calling <code>Persistable</code> instance
     * @param field the field number 
     * @param currentValue the current value of the field
     * @return the new value for the field
     */    
    String getStringField (Persistable pc, int field, String currentValue);
    
    /** 
     * Return the value for the field.
     * @param pc the calling <code>Persistable</code> instance
     * @param field the field number 
     * @param currentValue the current value of the field
     * @return the new value for the field
     */    
    Object getObjectField (Persistable pc, int field, Object currentValue);

    /** 
     * Mark the field as modified by the user.
     * @param pc the calling <code>Persistable</code> instance
     * @param field the field number
     * @param currentValue the current value of the field
     * @param newValue the proposed new value of the field */    
    void setBooleanField (Persistable pc, int field, boolean currentValue, boolean newValue);

    /** 
     * Mark the field as modified by the user.
     * @param pc the calling <code>Persistable</code> instance
     * @param field the field number
     * @param currentValue the current value of the field
     * @param newValue the proposed new value of the field */    
    void setCharField (Persistable pc, int field, char currentValue, char newValue);

    /** 
     * Mark the field as modified by the user.
     * @param pc the calling <code>Persistable</code> instance
     * @param field the field number
     * @param currentValue the current value of the field
     * @param newValue the proposed new value of the field */    
    void setByteField (Persistable pc, int field, byte currentValue, byte newValue);

    /** 
     * Mark the field as modified by the user.
     * @param pc the calling <code>Persistable</code> instance
     * @param field the field number
     * @param currentValue the current value of the field
     * @param newValue the proposed new value of the field */    
    void setShortField (Persistable pc, int field, short currentValue, short newValue);

    /** 
     * Mark the field as modified by the user.
     * @param pc the calling <code>Persistable</code> instance
     * @param field the field number
     * @param currentValue the current value of the field
     * @param newValue the proposed new value of the field */    
    void setIntField (Persistable pc, int field, int currentValue, int newValue);

    /** 
     * Mark the field as modified by the user.
     * @param pc the calling <code>Persistable</code> instance
     * @param field the field number
     * @param currentValue the current value of the field
     * @param newValue the proposed new value of the field */    
    void setLongField (Persistable pc, int field, long currentValue, long newValue);

    /** 
     * Mark the field as modified by the user.
     * @param pc the calling <code>Persistable</code> instance
     * @param field the field number
     * @param currentValue the current value of the field
     * @param newValue the proposed new value of the field */    
    void setFloatField (Persistable pc, int field, float currentValue, float newValue);

    /** 
     * Mark the field as modified by the user.
     * @param pc the calling <code>Persistable</code> instance
     * @param field the field number
     * @param currentValue the current value of the field
     * @param newValue the proposed new value of the field */    
    void setDoubleField (Persistable pc, int field, double currentValue, double newValue);

    /** 
     * Mark the field as modified by the user.
     * @param pc the calling <code>Persistable</code> instance
     * @param field the field number
     * @param currentValue the current value of the field
     * @param newValue the proposed new value of the field */    
    void setStringField (Persistable pc, int field, String currentValue, String newValue);

    /** 
     * Mark the field as modified by the user.
     * @param pc the calling <code>Persistable</code> instance
     * @param field the field number
     * @param currentValue the current value of the field
     * @param newValue the proposed new value of the field */    
    void setObjectField (Persistable pc, int field, Object currentValue, Object newValue);

    /** 
     * The value of the field requested to be provided to the <code>StateManager</code>.
     * @param pc the calling <code>Persistable</code> instance
     * @param field the field number 
     * @param currentValue the current value of the field
     */    
    void providedBooleanField (Persistable pc, int field, boolean currentValue);

    /** 
     * The value of the field requested to be provided to the <code>StateManager</code>.
     * @param pc the calling <code>Persistable</code> instance
     * @param field the field number 
     * @param currentValue the current value of the field
     */    
    void providedCharField (Persistable pc, int field, char currentValue);

    /** 
     * The value of the field requested to be provided to the <code>StateManager</code>.
     * @param pc the calling <code>Persistable</code> instance
     * @param field the field number 
     * @param currentValue the current value of the field
     */    
    void providedByteField (Persistable pc, int field, byte currentValue);

    /** 
     * The value of the field requested to be provided to the <code>StateManager</code>.
     * @param pc the calling <code>Persistable</code> instance
     * @param field the field number 
     * @param currentValue the current value of the field
     */    
    void providedShortField (Persistable pc, int field, short currentValue);

    /** 
     * The value of the field requested to be provided to the <code>StateManager</code>.
     * @param pc the calling <code>Persistable</code> instance
     * @param field the field number 
     * @param currentValue the current value of the field
     */    
    void providedIntField (Persistable pc, int field, int currentValue);

    /** 
     * The value of the field requested to be provided to the <code>StateManager</code>.
     * @param pc the calling <code>Persistable</code> instance
     * @param field the field number 
     * @param currentValue the current value of the field
     */    
    void providedLongField (Persistable pc, int field, long currentValue);

    /** 
     * The value of the field requested to be provided to the <code>StateManager</code>.
     * @param pc the calling <code>Persistable</code> instance
     * @param field the field number 
     * @param currentValue the current value of the field
     */    
    void providedFloatField (Persistable pc, int field, float currentValue);

    /** 
     * The value of the field requested to be provided to the <code>StateManager</code>.
     * @param pc the calling <code>Persistable</code> instance
     * @param field the field number 
     * @param currentValue the current value of the field
     */    
    void providedDoubleField (Persistable pc, int field, double currentValue);

    /** 
     * The value of the field requested to be provided to the <code>StateManager</code>.
     * @param pc the calling <code>Persistable</code> instance
     * @param field the field number 
     * @param currentValue the current value of the field
     */    
    void providedStringField (Persistable pc, int field, String currentValue);

    /** 
     * The value of the field requested to be provided to the <code>StateManager</code>.
     * @param pc the calling <code>Persistable</code> instance
     * @param field the field number 
     * @param currentValue the current value of the field
     */    
    void providedObjectField (Persistable pc, int field, Object currentValue);

    /** 
     * The replacement value of the field in the calling instance.
     * @param pc the calling <code>Persistable</code> instance
     * @param field the field number
     * @return the new value for the field
     */    
    boolean replacingBooleanField (Persistable pc, int field);

    /** 
     * The replacement value of the field in the calling instance.
     * @param pc the calling <code>Persistable</code> instance
     * @param field the field number 
     * @return the new value for the field
     */    
    char replacingCharField (Persistable pc, int field);

    /** 
     * The replacement value of the field in the calling instance.
     * @param pc the calling <code>Persistable</code> instance
     * @param field the field number 
     * @return the new value for the field
     */    
    byte replacingByteField (Persistable pc, int field);

    /** 
     * The replacement value of the field in the calling instance.
     * @param pc the calling <code>Persistable</code> instance
     * @param field the field number 
     * @return the new value for the field
     */    
    short replacingShortField (Persistable pc, int field);

    /** 
     * The replacement value of the field in the calling instance.
     * @param pc the calling <code>Persistable</code> instance
     * @param field the field number 
     * @return the new value for the field
     */    
    int replacingIntField (Persistable pc, int field);

    /** 
     * The replacement value of the field in the calling instance.
     * @param pc the calling <code>Persistable</code> instance
     * @param field the field number 
     * @return the new value for the field
     */    
    long replacingLongField (Persistable pc, int field);

    /** 
     * The replacement value of the field in the calling instance.
     * @param pc the calling <code>Persistable</code> instance
     * @param field the field number 
     * @return the new value for the field
     */    
    float replacingFloatField (Persistable pc, int field);

    /** 
     * The replacement value of the field in the calling instance.
     * @param pc the calling <code>Persistable</code> instance
     * @param field the field number 
     * @return the new value for the field
     */    
    double replacingDoubleField (Persistable pc, int field);

    /** 
     * The replacement value of the field in the calling instance.
     * @param pc the calling <code>Persistable</code> instance
     * @param field the field number 
     * @return the new value for the field
     */    
    String replacingStringField (Persistable pc, int field);

    /** 
     * The replacement value of the field in the calling instance.
     * @param pc the calling <code>Persistable</code> instance
     * @param field the field number 
     * @return the new value for the field
     */    
    Object replacingObjectField (Persistable pc, int field);
    
    /** 
     * The replacement value of the detached state in the calling instance.
     * @param pc the calling <code>Detachable</code> instance
     * @param state the current value of the detached state
     * @return the replacement value for the detached state
     */
    Object[] replacingDetachedState (Persistable pc, Object[] state);
}