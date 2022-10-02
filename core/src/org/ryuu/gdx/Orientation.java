package org.ryuu.gdx;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
public enum Orientation {
    Portrait(1, "Portrait"), Landscape(2, "Landscape");

    @Getter
    private final int id;
    @Getter
    private final String name;
}