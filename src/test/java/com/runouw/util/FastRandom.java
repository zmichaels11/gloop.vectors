/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.runouw.util;

import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

/**
 * Implementation of java.util.Random that utilizes either xorshift or
 * xorshift128+ for random number generation.
 *
 * @author zmichaels
 * @since 16.02.12
 */
@SuppressWarnings("serial")
public class FastRandom extends Random {

    private static final Marker MARKER = MarkerFactory.getMarker("RUTIL");
    private static final Logger LOGGER = LoggerFactory.getLogger(FastRandom.class);
    private final long[] seed;
    private final Semaphore semaphore = new Semaphore(1);

    private static final AtomicLong SEED_UNIQUIFIER
            = new AtomicLong(8682522807148012L);

    private static long seedUniquifier() {
        // L'Ecuyer, "Tables of Linear Congruential Generators of
        // Different Sizes and Good Lattice Structure", 1999
        for (;;) {
            long current = SEED_UNIQUIFIER.get();
            long next = current * 181783497276652981L;
            if (SEED_UNIQUIFIER.compareAndSet(current, next)) {
                return next;
            }
        }
    }

    private static long scramble(final long seed) {
        final long mask = (1L << 48) - 1;
        final long multiplier = 0x5DEECE66DL;

        return (seed ^ multiplier) & mask;
    }

    /**
     * Constructs a new FastRandom object. A 64bit seed based on the time will
     * be used.
     *
     * @since 16.02.12
     */
    public FastRandom() {
        this(seedUniquifier() ^ System.nanoTime());
    }

    /**
     * Constructs a new FastRandom object that uses xorshift and a 64bit seed.
     *
     * @param seed the 64bit seed.
     * @since 16.02.12
     */
    public FastRandom(final long seed) {
        this.seed = new long[1];
        this.seed[0] = scramble(seed);
    }

    /**
     * Constructs a new FastRandom object that uses xorshift128+ and two 64bit
     * seeds.
     *
     * @param lSeed the first 64bit seed.
     * @param hSeed the second 64bit seed.
     * @since 16.02.12
     */
    public FastRandom(final long lSeed, final long hSeed) {
        this.seed = new long[2];
        this.seed[0] = scramble(lSeed);
        this.seed[1] = scramble(hSeed);
    }

    /**
     * Checks if the pseudorandom number generator is using the 128bit xorshift
     * algorithm.
     *
     * @return true if xorshift128+ is being used.
     * @since 16.02.12
     */
    public boolean is128Bit() {
        return this.seed.length == 2;
    }

    private long xorshift() {
        long x = this.seed[0];
        x ^= (x << 21);
        x ^= (x >>> 35);
        x ^= (x << 4);
        this.seed[0] = x;
        return x;
    }

    private long xorshift128() {
        long x = this.seed[0];
        long y = this.seed[1];
        this.seed[0] = y;
        x ^= x << 23;
        this.seed[1] = x ^ y ^ (x >> 17) ^ (y << 26);
        return this.seed[1] + y;
    }

    @Override
    protected int next(int bits) {
        boolean solved = false;
        long result = 0L;

        try {
            semaphore.acquire();
            result = this.is128Bit() ? this.xorshift128() : this.xorshift();
            result &= ((1L << bits) - 1);
        } catch (InterruptedException ex) {
            LOGGER.warn(MARKER, "Unable to acquire lock on seed!");
            LOGGER.trace(MARKER, ex.getMessage(), ex);
        } finally {
            semaphore.release();
        }

        if (!solved) {
            return super.next(bits);
        } else {
            return (int) result;
        }
    }
}
