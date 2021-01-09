/*
 * jNoiseLib [https://github.com/andrewgp/jLibNoise]
 * Original code from libnoise [https://github.com/andrewgp/jLibNoise]
 *
 * Copyright (C) 2003, 2004 Jason Bevins
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or (at
 * your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public
 * License (COPYING.txt) for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * The developer's email is jlbezigvins@gmzigail.com (for great email, take
 * off every 'zig'.)
 */
package ConquerSpace.common.jLibNoise.noise.utils;


/**
 * Implements a noise map, a 2-dimensional array of floating-point values. A noise map is designed
 * to store coherent-noise values generated by a noise module, although it can store values from any
 * source. A noise map is often used as a terrain height map or a grayscale texture.
 * <p>
 * The size (width and height) of the noise map can be specified during object construction or at
 * any other time.
 * <p>
 * The GetValue() and SetValue() methods can be used to access individual values stored in the noise
 * map.
 * <p>
 * This class manages its own memory. If you copy a noise map object into another noise map object,
 * the original contents of the noise map object will be freed.
 * <p>
 * If you specify a new size for the noise map and the new size is smaller than the current size,
 * the allocated memory will not be reallocated. Call ReclaimMem() to reclaim the wasted memory.
 * <p>
 * <b>Border Values</b>
 * <p>
 * All of the values outside of the noise map are assumed to have a common value known as the
 * <i>border value</i>.
 * <p>
 * To set the border value, call the SetBorderValue() method.
 * <p>
 * The GetValue() method returns the border value if the specified value lies outside of the noise
 * map.
 * <p>
 * <b>Internal Noise Map Structure</b>
 * <p>
 * Internally, the values are organized into horizontal rows called @a slabs. Slabs are ordered from
 * bottom to top.
 * <p>
 * Each slab contains a contiguous row of values in memory. The values in a slab are organized left
 * to right.
 * <p>
 * The offset between the starting points of any two adjacent slabs is called the <i>stride
 * amount</i>. The stride amount is measured by the number of @a float values between these two
 * starting points, not by the number of bytes. For efficiency reasons, the stride is often a
 * multiple of the machine word size.
 * <p>
 * The GetSlabPtr() and GetConstSlabPtr() methods allow you to retrieve pointers to the slabs
 * themselves.
 * <p>
 * source 'noiseutils.h/cpp'
 */
public class NoiseMap {

    // The maximum width of a raster.
    public static int RASTER_MAX_WIDTH = 32767;
    // The maximum height of a raster.
    public static int RASTER_MAX_HEIGHT = 32767;
    // The raster's stride length must be a multiple of this constant.
    public static int RASTER_STRIDE_BOUNDARY = 4;

    // Value used for all positions outside of the noise map.
    private float borderValue;
    // The current height of the noise map.
    private int height;
    /// The amount of memory allocated for this noise map.
    /// This value is equal to the number of @a float values allocated for
    /// the noise map, not the number of bytes.
    private long memUsed;
    /// A pointer to the noise map buffer.
    private float[] noiseMap;
    // The stride amount of the noise map.
    private int stride;
    // The current width of the noise map.
    private int width;

    public NoiseMap() {
        initObj();
    }

    /**
     * Creates a noise map with uninitialized values.
     * <p>
     * It is considered an error if the specified dimensions are not positive.
     *
     * @param width The width of the new noise map.
     * @param height The height of the new noise map.
     * @throws IllegalArgumentException See the preconditions.
     */
    public NoiseMap(int width, int height) {
        initObj();
        setSize(width, height);
    }

    /**
     * Copy constructor.
     *
     */
    public NoiseMap(NoiseMap rhs) {
        initObj();
        copyNoiseMap(rhs);
    }

    /// Assignment operator.
    ///
    /// @throw noise::ExceptionOutOfMemory Out of memory.
    ///
    /// @returns Reference to self.
    ///
    /// Creates a copy of the noise map.
//    NoiseMap& operator= (const NoiseMap& rhs);
    /**
     * Clears the noise map to a specified value.
     *
     * @param value The value that all positions within the noise map are cleared to.
     */
    public void clear(float value) {
        if (noiseMap != null) {
            for (int i = 0; i < height * width; i++) {
                noiseMap[i] = value;
            }
        }
    }

    /**
     * Returns the value used for all positions outside of the noise map.
     * <p>
     * All positions outside of the noise map are assumed to have a common value known as the
     * <i>border value</i>.
     *
     * @return The value used for all positions outside of the noise map.
     */
    public float getBorderValue() {
        return borderValue;
    }

    /**
     * Returns a const pointer to a slab.
     *
     * @return A const pointer to a slab at the position (0, 0), or NULL if the noise map is empty.
     */
    @Deprecated
    public float[] getConstSlabPtr() {
        return noiseMap;
    }

    /**
     * Returns a const pointer to a slab at the specified row.
     * <p>
     * This method does not perform bounds checking so be careful when calling it.
     *
     * @param row The row, or @a y coordinate.
     * @return A const pointer to a slab at the position ( 0, @a row ), or @a NULL if the noise map
     * is empty. The coordinates must exist within the bounds of the noise map.
     */
    @Deprecated
    public ArrayPointer.NativeFloatPrim getConstSlabPtr(int row) {
        return getConstSlabPtr(0, row);
    }

    /**
     * Returns a const pointer to a slab at the specified position.
     * <p>
     * This method does not perform bounds checking so be careful when calling it.
     *
     * @param x The x coordinate of the position.
     * @param y The y coordinate of the position.
     * @return A const pointer to a slab at the position ( @a x, @a y ), or @a NULL if the noise map
     * is empty. The coordinates must exist within the bounds of the noise map.
     */
    public ArrayPointer.NativeFloatPrim getConstSlabPtr(int x, int y) {
        return new ArrayPointer.NativeFloatPrim(noiseMap, x + (y * width));
    }

    /**
     * Returns the height of the noise map.
     *
     * @return The height of the noise map.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns the amount of memory allocated for this noise map.
     * <p>
     * This method returns the number of @a float values allocated.
     *
     * @return The amount of memory allocated for this noise map.
     */
    public long getMemUsed() {
        return memUsed;
    }

    /**
     * Returns a pointer to a slab.
     *
     * @return A pointer to a slab at the position (0, 0), or @a NULL if the noise map is empty.
     */
    public float[] getSlabPtr() {
        return noiseMap;
    }

    /**
     * Returns a pointer to a slab at the specified row.
     * <p>
     * This method does not perform bounds checking so be careful when calling it.
     *
     * @param row The row, or @a y coordinate.
     * @return A pointer to a slab at the position ( 0, @a row ), or @a NULL if the noise map is
     * empty. The coordinates must exist within the bounds of the noise map.
     */
    public ArrayPointer.NativeFloatPrim getSlabPtr(int row) {
        return getSlabPtr(0, row);
    }

    /**
     * Returns a pointer to a slab at the specified position.
     * <p>
     * This method does not perform bounds checking so be careful when calling it.
     *
     * @param x The x coordinate of the position.
     * @param y The y coordinate of the position.
     * @return A pointer to a slab at the position ( @a x, @a y ) or @a NULL if the noise map is
     * empty. The coordinates must exist within the bounds of the noise map.
     */
    public ArrayPointer.NativeFloatPrim getSlabPtr(int x, int y) {
//        return m_pNoiseMap + (long) x + (long) m_stride * (long) y;
        return new ArrayPointer.NativeFloatPrim(noiseMap, x * y);
    }

    /**
     * Returns the stride amount of the noise map.
     * <p>
     * - The <i>stride amount</i> is the offset between the starting points of any two adjacent
     * slabs in a noise map. - The stride amount is measured by the number of @a float values
     * between these two points, not by the number of bytes.
     *
     * @return The stride amount of the noise map.
     */
    public int getStride() {
        return stride;
    }

    /**
     * Returns a value from the specified position in the noise map.
     * <p>
     * This method returns the border value if the coordinates exist outside of the noise map.
     *
     * @param x The x coordinate of the position.
     * @param y The y coordinate of the position.
     * @return The value at that position.
     */
    public float getValue(int x, int y) {
        if (noiseMap != null) {
            if (x >= 0 && x < width && y >= 0 && y < height) {
                return noiseMap[x * y];
            }
        }
        // The coordinates specified are outside the noise map.  Return the border
        // value.
        return borderValue;
    }

    /**
     * Returns the width of the noise map.
     *
     * @return The width of the noise map.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Reallocates the noise map to recover wasted memory.
     * <p>
     * The contents of the noise map is unaffected.
     * <p>
     * throws ExceptionOutOfMemory Out of memory. (Yes, this method can return an out-of-memory
     * exception because two noise maps will temporarily exist in memory during this call.)
     */
    public void reclaimMem() {
//        size_t newMemUsage = CalcMinMemUsage(m_width, m_height);
//        if (m_memUsed > newMemUsage) {
//            // There is wasted memory.  Create the smallest buffer that can fit the
//            // data and copy the data to it.
//            float*pNewNoiseMap = NULL;
//            try {
//                pNewNoiseMap = new float[newMemUsage];
//            } catch (...){
//                throw noise::ExceptionOutOfMemory();
//            }
//            memcpy(pNewNoiseMap, m_pNoiseMap, newMemUsage * sizeof(float));
//            delete[] m_pNoiseMap;
//            m_pNoiseMap = pNewNoiseMap;
//            m_memUsed = newMemUsage;
//        }
        throw new UnsupportedOperationException();
    }

    /**
     * Sets the value to use for all positions outside of the noise map.
     * <p>
     * All positions outside of the noise map are assumed to have a common value known as the
     * <i>border value</i>.
     *
     * @param borderValue The value to use for all positions outside of the noise map.
     */
    public void setBorderValue(float borderValue) {
        this.borderValue = borderValue;
    }

    /**
     * Sets the new size for the noise map.
     * <p>
     * On exit, the contents of the noise map are undefined. If the @a OUT_OF_MEMORY exception
     * occurs, this noise map object becomes empty. If the @a INVALID_PARAM exception occurs, the
     * noise map is unmodified.
     *
     * @param width The new width for the noise map.
     * @param height The new height for the noise map.
     * @throws IllegalArgumentException See the preconditions.
     */
    public void setSize(int width, int height) {
        if (width < 0 || height < 0
                || width > RASTER_MAX_WIDTH || height > RASTER_MAX_HEIGHT) {
            // Invalid width or height.
            throw new IllegalArgumentException("Invalid width or height");
        } else if (width == 0 || height == 0) {
            // An empty noise map was specified.  Delete it and zero out the size
            // member variables.
            deleteNoiseMapAndReset();
        } else {
            // A new noise map size was specified.  Allocate a new noise map buffer
            // unless the current buffer is large enough for the new noise map (we
            // don't want costly reallocations going on.)
            long newMemUsage = calcMinMemUsage(width, height);
            if (memUsed < newMemUsage) {
                // The new size is too big for the current noise map buffer.  We need to
                // reallocate.
                deleteNoiseMapAndReset();
                noiseMap = new float[(int) newMemUsage];

                memUsed = newMemUsage;
            }
            stride = (int) calcStride(width);
            this.width = width;
            this.height = height;
        }
    }

    /**
     * Sets a value at a specified position in the noise map.
     * <p>
     * This method does nothing if the noise map object is empty or the position is outside the
     * bounds of the noise map.
     *
     * @param x The x coordinate of the position.
     * @param y The y coordinate of the position.
     * @param value The value to set at the given position.
     */
    public void setValue(int x, int y, float value) {
        if (noiseMap != null) {
            if (x >= 0 && x < width && y >= 0 && y < height) {
                noiseMap[x * y] = value;
            }
        }
    }

    /**
     * Takes ownership of the buffer within the source noise map.
     * <p>
     * On exit, the source noise map object becomes empty.
     * <p>
     * This method only moves the buffer pointer so this method is very quick.
     *
     * @param source The source noise map.
     */
    public void takeOwnership(NoiseMap source) {
        // Copy the values and the noise map buffer from the source noise map to
        // this noise map.  Now this noise map pwnz the source buffer.
//        delete[] m_pNoiseMap;
        memUsed = source.memUsed;
        height = source.height;
        noiseMap = source.noiseMap;
        stride = source.stride;
        width = source.width;

        // Now that the source buffer is assigned to this noise map, reset the
        // source noise map object.
        source.initObj();
    }

    /**
     * Returns the minimum amount of memory required to store a noise map of the specified size.
     * <p>
     * The returned value is measured by the number of @a float values required to store the noise
     * map, not by the number of bytes.
     *
     * @param width The width of the noise map.
     * @param height The height of the noise map.
     * @return The minimum amount of memory required to store the noise map.
     */
    private long calcMinMemUsage(int width, int height) {
        return calcStride(width * height);
    }

    /**
     * Calculates the stride amount for a noise map.
     * <p>
     * - The <i>stride amount</i> is the offset between the starting points of any two adjacent
     * slabs in a noise map. - The stride amount is measured by the number of @a float values
     * between these two points, not by the number of bytes.
     *
     * @param width The width of the noise map.
     * @return The stride amount.
     */
    private long calcStride(int width) {
        return width;//(long) (((width + RASTER_STRIDE_BOUNDARY - 1) / RASTER_STRIDE_BOUNDARY) * RASTER_STRIDE_BOUNDARY);
    }

    /**
     * Copies the contents of the buffer in the source noise map into this noise map.
     * <p>
     * This method reallocates the buffer in this noise map object if necessary.
     *
     * @param source The source noise map.
     * @throws ConquerSpace.common.jLibNoise.noise.ExceptionOutOfMemory Out of memory.
     *
     * @warning This method calls the standard library function
     * @a memcpy, which probably violates the DMCA because it can be used to make a bitwise copy of
     * anything, like, say, a DVD. Don't call this method if you live in the USA.
     */
    private void copyNoiseMap(NoiseMap source) {
        // Resize the noise map buffer, then copy the slabs from the source noise
        // map buffer to this noise map buffer.
        setSize(source.getWidth(), source.getHeight());
        for (int y = 0; y < source.getHeight(); y++) {
            ArrayPointer.NativeFloatPrim src = source.getConstSlabPtr(0, y);
            ArrayPointer.NativeFloatPrim dest = getSlabPtr(0, y);
            src.copyTo(dest, source.getWidth());
        }

        // Copy the border value as well.
        borderValue = source.borderValue;
    }

    /**
     * Resets the noise map object.
     * <p>
     * This method is similar to the InitObj() method, except this method deletes the buffer in this
     * noise map.
     */
    private void deleteNoiseMapAndReset() {
        initObj();
    }

    /**
     * Initializes the noise map object.
     * <p>
     * Must be called during object construction. The noise map buffer must not exist.
     */
    private void initObj() {
        noiseMap = null;
        height = 0;
        width = 0;
        stride = 0;
        memUsed = 0;
        borderValue = 0.0f;
    }
}
