/*
 * The MIT License
 *
 * Copyright 2017 Zyun.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package ConquerSpace.start.gui;

/**
 *
 * @author Zyun
 */
public class UniverseConfig {
    public String universeSize;
    public String universeShape;
    public String universeAge;
    public String planetCommonality;
    public String civilizationCount;

    public void setCivilizationCount(String civilizationCount) {
        this.civilizationCount = civilizationCount;
    }

    public void setPlanetCommonality(String planetCommonality) {
        this.planetCommonality = planetCommonality;
    }

    public void setUniverseAge(String universeAge) {
        this.universeAge = universeAge;
    }

    public void setUniverseShape(String universeShape) {
        this.universeShape = universeShape;
    }

    public void setUniverseSize(String universeSize) {
        this.universeSize = universeSize;
    }

    public String getCivilizationCount() {
        return civilizationCount;
    }

    public String getPlanetCommonality() {
        return planetCommonality;
    }

    public String getUniverseAge() {
        return universeAge;
    }

    public String getUniverseShape() {
        return universeShape;
    }

    public String getUniverseSize() {
        return universeSize;
    }
}
