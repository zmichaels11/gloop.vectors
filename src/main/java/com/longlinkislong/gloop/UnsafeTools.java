/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.longlinkislong.gloop;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import sun.misc.Unsafe;

/**
 * UnsafeUtils exposes a bridge between the Java heap and the standard heap.
 * 
 * @author zmichaels
 * @since 15.07.31
 */
public class UnsafeTools {

    private final Unsafe unsafe;

    private UnsafeTools() {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            this.unsafe = (Unsafe) f.get(null);
        } catch (Exception x) {
            throw new RuntimeException("Unable to retrieve unsafe!");
        }

        final boolean is64Bit = unsafe.addressSize() == Long.BYTES;

        if (!is64Bit) {
            throw new UnsupportedOperationException("UnsafeTools currently only works with 64bit uncompressed pointers!");
        }
    }

    /**
     * Calculates the size of an Object.
     *
     * @param obj the object to check.
     * @return the size of the object in bytes.
     * @since 15.07.31
     */
    public long sizeOf(final Object obj) {
        if (obj.getClass().isArray()) {
            final long header = unsafe.arrayBaseOffset(obj.getClass());
            final long index = unsafe.arrayIndexScale(obj.getClass());

            if (obj instanceof Object[]) {
                return ((Object[]) obj).length * index + header;
            }
        }
        return sizeOf(obj.getClass());
    }

    /**
     * Retrieves the size of a class.
     *
     * @param klazz the class
     * @return the size of the class in bytes.
     * @since 15.07.31
     */
    public long sizeOf(final Class<?> klazz) {
        final Set<Field> fields = new HashSet<>();

        Class currentClass = klazz;
        while (currentClass != Object.class) {
            Arrays.stream(currentClass.getDeclaredFields())
                    .filter(f -> (f.getModifiers() & Modifier.STATIC) == 0)
                    .forEach(fields::add);

            currentClass = currentClass.getSuperclass();
        }

        final long size = fields.stream()
                .mapToLong(unsafe::objectFieldOffset)
                .max().getAsLong();

        return ((size / Long.BYTES) + 1) * Long.BYTES; // pad to nearest long
    }

    /**
     * Copies the object off of the heap.
     *
     * @param <T> the type of object.
     * @param obj the object.
     * @return pointer to the object off of the heap.
     * @since 15.07.31
     */
    public <T> Pointer<T> moveOffHeap(final T obj) {
        final long size = sizeOf(obj);
        final long ptr = unsafe.allocateMemory(size);

        unsafe.copyMemory(obj, 0, null, ptr, size);

        return new Pointer<>(ptr);
    }

    /**
     * Allocates an object off of the heap.
     *
     * @param <T> the type of object.
     * @param klazz the class.
     * @return pointer to the new object.
     * @throws InstantiationException if a reference class cannot be made.
     * @throws UnsupportedOperationException if the class is an array.
     * @since 15.07.31
     */
    public <T> Pointer<T> malloc(Class<T> klazz) throws InstantiationException {
        if (klazz.isArray()) {
            throw new UnsupportedOperationException("Cannot allocate object arrays off of heap! Object arrays need to be moved off heap.");
        }

        final T ref = (T) unsafe.allocateInstance(klazz);
        final long size = sizeOf(ref);
        final long ptr = unsafe.allocateMemory(size);
        final long klassPtr = unsafe.getLong(ref, 8L);

        unsafe.putLong(ptr, 1L);                // write mark word
        unsafe.putLong(ptr + 8L, klassPtr);     // write the pointer to the meta class
        unsafe.putLong(ptr + 16L, 0L);          // pad the rest of the header with 0s

        return new Pointer<>(ptr);
    }

    /**
     * Allocates a float array off heap.
     *
     * @param size the number of floats to allocate.
     * @return pointer to the float array.
     * @since 15.07.31
     */
    public Pointer<float[]> fAlloc(int size) {
        final float[] ref = new float[0]; // just needed for the header
        final long klassPtr = unsafe.getLong(ref, 8L);
        final long header = unsafe.arrayBaseOffset(ref.getClass());
        final long pSize = header + size * Float.BYTES;
        final long ptr = unsafe.allocateMemory(pSize);

        unsafe.putLong(ptr, 1L);                // mark word
        unsafe.putLong(ptr + 8L, klassPtr);     // meta class pointer
        unsafe.putInt(ptr + 16L, size);         // array size
        unsafe.putInt(ptr + 20L, 0);            // padding

        return new Pointer<>(ptr);
    }

    /**
     * Allocates an int array off heap.
     *
     * @param size the number of floats to allocate.
     * @return pointer to the int array.
     * @since 15.07.31
     */
    public Pointer<int[]> iAlloc(int size) {
        final int[] ref = new int[0];
        final long klassPtr = unsafe.getLong(ref, 8L);
        final long header = unsafe.arrayBaseOffset(ref.getClass());
        final long pSize = header + size * Integer.BYTES;
        final long ptr = unsafe.allocateMemory(pSize);

        unsafe.putLong(ptr, 1L);
        unsafe.putLong(ptr + 8L, klassPtr);
        unsafe.putInt(ptr + 16L, size);
        unsafe.putInt(ptr + 20L, 0);

        return new Pointer<>(ptr);
    }

    /**
     * Allocates a long array off heap.
     *
     * @param size the number of longs to allocate.
     * @return pointer to the long array.
     * @since 15.07.31
     */
    public Pointer<long[]> lAlloc(int size) {
        final long[] ref = new long[0];
        final long klassPtr = unsafe.getLong(ref, 8L);
        final long header = unsafe.arrayBaseOffset(ref.getClass());
        final long pSize = header + size * Long.BYTES;
        final long ptr = unsafe.allocateMemory(pSize);

        unsafe.putLong(ptr, 1L);
        unsafe.putLong(ptr + 8L, klassPtr);
        unsafe.putInt(ptr + 16L, size);
        unsafe.putInt(ptr + 20L, 0);

        return new Pointer<>(ptr);
    }

    /**
     * Allocates a double array off heap.
     *
     * @param size the number of doubles to allocate.
     * @return pointer to the double array.
     * @since 15.07.31
     */
    public Pointer<double[]> dAlloc(int size) {
        final double[] ref = new double[0];
        final long klassPtr = unsafe.getLong(ref, 8L);
        final long header = unsafe.arrayBaseOffset(ref.getClass());
        final long pSize = header + size * Double.BYTES;
        final long ptr = unsafe.allocateMemory(pSize);

        unsafe.putLong(ptr, 1L);
        unsafe.putLong(ptr + 8L, klassPtr);
        unsafe.putInt(ptr + 16L, size);
        unsafe.putInt(ptr + 20L, 0);

        return new Pointer<>(ptr);
    }

    /**
     * Allocates a byte array off heap.
     *
     * @param size the number of bytes to allocate.
     * @return pointer to the byte array.
     * @since 15.07.31
     */
    public Pointer<byte[]> bAlloc(int size) {
        final byte[] ref = new byte[0];
        final long klassPtr = unsafe.getLong(ref, 8L);
        final long header = unsafe.arrayBaseOffset(ref.getClass());
        final long pSize = header + size * Byte.BYTES;
        final long ptr = unsafe.allocateMemory(pSize);

        unsafe.putLong(ptr, 1L);
        unsafe.putLong(ptr + 8L, klassPtr);
        unsafe.putInt(ptr + 16L, size);
        unsafe.putInt(ptr + 20L, 0);

        return new Pointer<>(ptr);
    }

    /**
     * Allocates a short array off heap.
     *
     * @param size the number of shorts to allocate.
     * @return pointer to the short array.
     * @since 15.07.31
     */
    public Pointer<short[]> sAlloc(int size) {
        final short[] ref = new short[0];
        final long klassPtr = unsafe.getLong(ref, 8L);
        final long header = unsafe.arrayBaseOffset(ref.getClass());
        final long pSize = header + size * Short.BYTES;
        final long ptr = unsafe.allocateMemory(pSize);

        unsafe.putLong(ptr, 1L);
        unsafe.putLong(ptr + 8L, klassPtr);
        unsafe.putInt(ptr + 16L, size);
        unsafe.putInt(ptr + 20L, 0);

        return new Pointer<>(ptr);
    }

    private long pOffset = 0L;

    private long getPointerOffset() {
        if (this.pOffset == 0L) {
            try {
                return this.pOffset = unsafe.objectFieldOffset(Pointer.class.getDeclaredField("instance"));
            } catch (NoSuchFieldException | SecurityException ex) {
                throw new RuntimeException("Unable to get location of internal pointer!");
            }
        } else {
            return this.pOffset;
        }
    }

    /**
     * Pointer is a wrapper that wraps an object allocated off heap.
     * @param <T> the type of object.
     * @since 15.07.31
     */
    public class Pointer<T> {

        /**
         * Instance of the object off heap.
         * @since 15.07.31
         */
        public final T instance = null;        
        private final long ptr;

        Pointer(long ptr) {
            final long off = getPointerOffset();

            unsafe.putLong(this, off, this.ptr = ptr);
        }

        /**
         * Frees memory used by the off heap object.
         * @since 15.07.31
         */
        public void free() {
            unsafe.freeMemory(this.ptr);
            unsafe.putLong(this, getPointerOffset(), 0L); //reset to null
        }
    }

    private static class Holder {

        static final UnsafeTools INSTANCE = new UnsafeTools();
    }

    /**
     * Retrieves the instance.
     * @return the instance.
     * @since 15.07.31
     */
    public static UnsafeTools getInstance() {
        return Holder.INSTANCE;
    }
}
