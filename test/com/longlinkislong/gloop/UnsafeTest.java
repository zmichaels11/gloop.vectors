/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.longlinkislong.gloop;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import sun.misc.Unsafe;

/**
 *
 * @author zmichaels
 */
public class UnsafeTest {

    static Unsafe getUnsafe() {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            return (Unsafe) f.get(null);
        } catch (Exception x) {
            x.printStackTrace();
        }

        return null;
    }

    public static long sizeOf(Object o) {
        Unsafe u = getUnsafe();

        Set<Field> fields = new HashSet<Field>();
        Class c = o.getClass();
        while (c != Object.class) {
            for (Field f : c.getDeclaredFields()) {
                if ((f.getModifiers() & Modifier.STATIC) == 0) {
                    fields.add(f);
                }
            }
            c = c.getSuperclass();
        }

        // get offset
        long maxSize = 0;
        for (Field f : fields) {
            long offset = u.objectFieldOffset(f);
            if (offset > maxSize) {
                maxSize = offset;
            }
        }

        return ((maxSize / 8) + 1) * 8;   // padding
    }

    @Test
    public void testSizeOf() {
        MyStruct test = new MyStruct();
        System.out.printf("size: %d\n", sizeOf(test));
    }

    @Before
    public void testAddressSize() {
        // confirm using uncompressed oops
        Assert.assertEquals(Long.BYTES, getUnsafe().addressSize());
    }

    @Test
    public void testOffHeapMove() throws NoSuchFieldException {
        final MyStruct heapObj = new MyStruct();
        heapObj.x = 1234f;
        heapObj.y = 4321f;

        final long size = sizeOf(heapObj);
        final Unsafe unsafe = getUnsafe();
        final long ptr = unsafe.allocateMemory(size);

        unsafe.copyMemory(heapObj, 0, null, ptr, size);

        final Pointer<MyStruct> offHeapObj = new Pointer<>();

        unsafe.putLong(offHeapObj, unsafe.objectFieldOffset(Pointer.class.getDeclaredField("p")), ptr);

        Assert.assertEquals(heapObj.x, offHeapObj.p.x, 0.1f);
        Assert.assertEquals(heapObj.y, offHeapObj.p.y, 0.1f);

        unsafe.freeMemory(ptr);
    }

    @Test
    public void testOffHeapAllocate() throws Exception {
        final MyStruct ref = new MyStruct();
        final Unsafe unsafe = getUnsafe();
        final long ptr = unsafe.allocateMemory(sizeOf(ref));
        final long klassPtr = unsafe.getLong(ref, 8L);

        unsafe.putLong(ptr, 1L);
        unsafe.putLong(ptr + 8L, klassPtr);
        unsafe.putLong(ptr + 16L, 0L); // size + padding

        final Pointer<MyStruct> p = new Pointer<>();

        unsafe.putLong(p, unsafe.objectFieldOffset(Pointer.class.getDeclaredField("p")), ptr);

        p.p.x = 12345f;
        p.p.y = 54321f;

        System.out.println(p.p.x);
        System.out.println(p.p.y);
    }

    @Test
    public void testReadArray() {
        float[] ref = {1f, 2f, 3f, 4f};
        final Unsafe unsafe = getUnsafe();

        Assert.assertEquals(ref[0], unsafe.getFloat(ref, 24L), 0.1f);
        Assert.assertEquals(ref[1], unsafe.getFloat(ref, 28L), 0.1f);
        Assert.assertEquals(ref[2], unsafe.getFloat(ref, 32L), 0.1f);
        Assert.assertEquals(ref[3], unsafe.getFloat(ref, 36L), 0.1f);

        System.out.printf("mark word: %d\n", unsafe.getLong(ref, 0L));
        System.out.printf("klass ptr: %d\n", unsafe.getLong(ref, 8L));
        System.out.printf("array length: %d\n", unsafe.getInt(ref, 16L));
        System.out.printf("ref[0] = %f\n", unsafe.getFloat(ref, 24L));
        System.out.printf("ref[1] = %f\n", unsafe.getFloat(ref, 28L));
        System.out.printf("ref[2] = %f\n", unsafe.getFloat(ref, 32L));
        System.out.printf("ref[3] = %f\n", unsafe.getFloat(ref, 36L));
    }

    @Test
    public void testOffHeapArray() throws NoSuchFieldException, InstantiationException {
        final Unsafe unsafe = getUnsafe();
        float[] ref = new float[0];

        final long klassPtr = unsafe.getLong(ref, 8L);
        final long ptr = unsafe.allocateMemory(40L);

        System.out.printf("klassPtr: %d\n", klassPtr);

        System.out.println("building header...");
        unsafe.putLong(ptr, 1L);
        unsafe.putLong(ptr + 8L, klassPtr);
        unsafe.putInt(ptr + 16L, 4);
        System.out.println("writing values");
        unsafe.putFloat(ptr + 24L, 1f);
        unsafe.putFloat(ptr + 28L, 2f);
        unsafe.putFloat(ptr + 32L, 3f);
        unsafe.putFloat(ptr + 36L, 4f);

        final Pointer<float[]> p = new Pointer<>(ptr);
        
        Assert.assertArrayEquals(new float[]{1f, 2f, 3f, 4f}, p.p, 0.1f);

        p.free();
    }
    
    @Test
    public void testObjectArray() {
        MyStruct[] array = new MyStruct[4];
        Object[] genArray = new Object[4];
        Unsafe unsafe = getUnsafe();
        
        array[0] = new MyStruct();
        array[0].x = 123456f;
        array[0].y = 654321f;
        
        System.out.printf("ptrKlass(MyStruct[])\t= %d\n", unsafe.getLong(array, 8L));
        System.out.printf("ptrKlass(Object[])\t= %d\n", unsafe.getLong(genArray, 8L));
        System.out.printf("ptrKlass(MyStruct)\t= %d\n", unsafe.getLong(array[0], 8L));
        
        long baseOffset = unsafe.arrayBaseOffset(MyStruct[].class);
        System.out.printf("address(array)\t= %d\n", unsafe.getLong(array, baseOffset));
        System.out.printf("array(24L)\t= %d\n", unsafe.getLong(array, 24L));
        
    }

    class MyStruct {

        float x;
        float y;
    }

    class Pointer<T> {

        final T p = null;
        final long ptr;

        Pointer() {
            this.ptr = 0L;
        }

        Pointer(final long ptr) throws NoSuchFieldException {
            final Unsafe unsafe = getUnsafe();
            final long off = unsafe.objectFieldOffset(Pointer.class.getDeclaredField("p"));

            unsafe.putLong(this, off, this.ptr = ptr);
        }

        void free() {
            getUnsafe().freeMemory(this.ptr);
        }
    }
}
