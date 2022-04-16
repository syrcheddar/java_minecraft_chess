module Chess {
    requires javafx.graphics;
    requires javafx.media;
    requires javafx.controls;
    requires java.logging;

    exports classes.pieces;
    exports classes.rest;

    opens classes.pieces;
    opens classes.rest;
}