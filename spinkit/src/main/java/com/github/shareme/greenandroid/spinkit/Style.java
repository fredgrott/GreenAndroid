/*
 Copyright ybq 2016
 Modifications Copyright(C) 2016 Fred Grott(GrottWorkShop)

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

	    http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.

 */
package com.github.shareme.greenandroid.spinkit;

/**
 * Created by ybq.
 */
public enum Style {

    ROTATING_PLANE(0),
    DOUBLE_BOUNCE(1),
    WAVE(2),
    WANDERING_CUBES(3),
    PULSE(4),
    CHASING_DOTS(5),
    THREE_BOUNCE(6),
    CIRCLE(7),
    CUBE_GRID(8),
    FADING_CIRCLE(9),
    FOLDING_CUBE(10);

    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private int value;

    Style(int value) {
        this.value = value;
    }
}
