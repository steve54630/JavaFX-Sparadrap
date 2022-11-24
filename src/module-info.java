module ECF_Java_FX {
	requires javafx.graphics;
    requires javafx.fxml;
	requires org.junit.platform.suite.api;
	requires org.junit.jupiter.api;
	requires java.sql;
	requires javafx.controls;
	requires java.desktop;
	requires javafx.swing;
	
	opens controller to javafx.graphics, javafx.fxml;
	opens model to javafx.base;
	opens application;
}
