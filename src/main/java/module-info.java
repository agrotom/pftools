module com.at.pftools20 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires org.json;
    requires org.apache.commons.io;
    requires log4j;
    requires org.jetbrains.annotations;
	requires javafx.base;
	requires javafx.graphics;
	
    opens com.at.pftools20 to javafx.fxml;
    exports com.at.pftools20;
    
    exports com.at.pftools20.logic.enums to com.at.pftools20;
    exports com.at.pftools20.logic.statcard.main to com.at.pftools20;
    exports com.at.pftools20.logic.props.main to com.at.pftools20.statcard.main;
}